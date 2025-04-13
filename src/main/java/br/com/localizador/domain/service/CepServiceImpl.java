package br.com.localizador.domain.service;

import br.com.localizador.domain.dto.ViagemDto;
import br.com.localizador.domain.entity.Cep;
import br.com.localizador.domain.repository.CepRepository;
import br.com.localizador.domain.service.exceptions.BusinessException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Service
@Transactional(readOnly = true)
@Log4j2
public class CepServiceImpl implements CepService {

    private final CepRepository cepRepository;

    public CepServiceImpl(CepRepository cepRepository) {
        this.cepRepository = cepRepository;
    }

    @Override
    @Transactional
    public Cep extrairCepsDeArquivo(MultipartFile arquivo) throws IOException {
        var cepValor = 0;
        var ceps = new ArrayList<Cep>();
        var bytes = arquivo.getBytes();
        var resultado = new Cep();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)))) {
            String linha;

            while ((linha = br.readLine()) != null) {
                if (linha.equals("--")) {
                    linha = br.readLine();

                    cepValor = Integer.parseInt(linha);
                    log.debug("CEP PARA VERIFICAR - ({}))", cepValor);

                    break;
                }

                String[] partes = linha.split(",");

                var cidade = partes[0];
                var cepInicio = partes[1];
                var cepFim = partes[2];

                var cep = new Cep(cidade, cepInicio, cepFim);

                ceps.add(this.registrarCep(cep));
                log.debug("CEP({}, {} {}) registrada com sucesso!)", cidade, cepInicio, cepFim);
            }

            resultado = this.buscarPorCepInRange(ceps, cepValor, null, null);

        } catch (IOException exc) {
            throw new BusinessException("Erro ao ler arquivo");
        }


        return resultado;
    }

    @Transactional
    @Override
    public Cep registrarCep(Cep cep) {
        if (cep.getCidade() == null || cep.getCidade().isEmpty() ||
                cep.getCepInicio() == null || cep.getCepInicio().isEmpty() ||
                cep.getCepFim() == null || cep.getCepFim().isEmpty())
            throw new BusinessException("Erro ao cadastrar CEP, revisar documento.");

        return cepRepository.registrarCep(cep);
    }

    private Cep buscarPorCepInRange(List<Cep> ceps, Integer cepValor, Integer cepValorOrigem, Integer cepValorDestino) {
        for (var cep : ceps) {
            var cepInicial = Integer.parseInt(cep.getCepInicio());
            var cepFinal = Integer.parseInt(cep.getCepFim());

            if (cepValor != null && cepValor >= 0 && cepInicial <= cepValor && cepFinal >= cepValor)
                return cep;

            if (cepValorOrigem != null &&
                    cepValorOrigem >= 0 && cepInicial <= cepValorOrigem &&
                    cepFinal >= cepValorOrigem)
                return cep;

            if (cepValorDestino != null &&
                    cepValorDestino >= 0 &&
                    cepInicial <= cepValorDestino &&
                    cepFinal >= cepValorDestino)
                return cep;
        }

        return null;
    }

    //TODO: Há Boilerplate em 2 métodos, refatorar no futuro
    @Override
    public ViagemDto calcularRotaMaisBarata(MultipartFile arquivo) throws IOException {
        var cidades = new HashMap<String, Cep>();
        var conexoes = new HashMap<String, Map<String, Double>>();
        var ceps = new ArrayList<Cep>();
        var contador = 0;
        var cidadeOrigem = new Cep();
        var cidadeDestino = new Cep();

        var bytes = arquivo.getBytes();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)))) {
            String linhas;

            while ((linhas = br.readLine()) != null) {
                if (contador == 0) {
                    if (linhas.equals("--")) {
                        linhas = br.readLine();
                        contador++;
                    } else { //REGISTRANDO OS CEPS
                        String[] partes = linhas.split(",");
                        var cidade = partes[0];
                        var cepInicial = partes[1];
                        var cepFinal = partes[2];
                        var cep = new Cep(cidade, cepInicial, cepFinal);

                        ceps.add(this.registrarCep(cep));

                        cidades.put(cidade, cep);
                    }

                }

                if (contador == 1) {
                    if (linhas.equals("--")) {
                        contador++;
                    } else { //REGISTRANDO AS CONEXÕES
                        String[] partes = linhas.split(",");
                        var partida = partes[0];
                        var destino = partes[1];
                        var valor = partes[2];

                        conexoes.put(partida, new HashMap<>());
                        conexoes.get(partes[0]).put(destino, Double.valueOf(valor));
                    }
                }

                if (contador == 2) {
                    if (linhas.equals("--")) { //PEGANDO CIDADE ORIGEM E DESTINO
                        linhas = br.readLine();
                        String[] partes = linhas.split(",");

                        var origem = partes[0];
                        cidadeOrigem = this.buscarPorCepInRange(ceps,
                                null, Integer.valueOf(origem), null);

                        var destino = partes[1];
                        cidadeDestino = this.buscarPorCepInRange(ceps, null,
                                null, Integer.valueOf(destino));
                    }
                }
            }
        } catch (IOException exc) {
            throw new BusinessException("Erro ao ler arquivo.");
        }

        return this.dijkstraMethod(conexoes, cidadeOrigem.getCidade(), cidadeDestino.getCidade());
    }


    private ViagemDto dijkstraMethod(Map<String, Map<String, Double>> grafo, String origem, String destino) {
        var custoDasConexoes = new HashMap<String, Double>();
        var rotaPai = new HashMap<String, String>();
        var visitado = new HashSet<String>();
        var fila = new PriorityQueue<>(Comparator.comparingDouble(custoDasConexoes::get));

        for (String chave : grafo.keySet())
            custoDasConexoes.put(chave, Double.MAX_VALUE);


        custoDasConexoes.put(origem, 0.0);
        fila.add(origem);

        while (!fila.isEmpty()) { //FAZENDO AS PASSAGENS DA ORIGEM ATÉ DESTINO
            String atual = fila.poll().toString();

            if (!visitado.add(atual)) continue;
            if (!grafo.containsKey(atual)) continue;

            for (var vizinho : grafo.get(atual).entrySet()) {
                var novoCusto = custoDasConexoes.get(atual) + vizinho.getValue();

                if (novoCusto < custoDasConexoes.getOrDefault(vizinho.getKey(), Double.MAX_VALUE)) {
                    custoDasConexoes.put(vizinho.getKey(), novoCusto);
                    rotaPai.put(vizinho.getKey(), atual);
                    fila.add(vizinho.getKey());
                }
            }
        }

        List<String> rota = new LinkedList<>();

        String atual = destino;

        while (atual != null) {
            rota.add(0, atual);
            atual = rotaPai.get(atual);
        }

        return new ViagemDto(rota, custoDasConexoes.getOrDefault(destino, Double.MAX_VALUE));
    }

}
