<h1>🏆 Prova Técnica - CEP</h1>

<h2>📌 Requisitos</h2>

<p>Antes de iniciar, certifique-se de ter os seguintes softwares instalados:</p>

<ul>
    <li>✅ <b>JDK 17</b></li>
    <li>✅ <b>Maven 3</b></li>
    <li>✅ <b>PostgreSQL 16</b></li>
</ul>

<hr/>

<h2>🛠️ Configuração do Banco de Dados</h2>

<h3>1. Criar um banco de dados no PostgreSQL:</h3>

<pre><code class="sql">
CREATE DATABASE nome_do_banco;
</code></pre>

<h2>📄 Configuração da Aplicação</h2>

<pre><code class="properties">
spring.datasource.url=jdbc:postgresql://localhost:5432/nome_do_banco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=none
spring.liquibase.change-log=classpath:/db/changelog/db.changelog-master.xml
</code></pre>

<hr/>

<h2>🔧 Tecnologias Utilizadas</h2>

<ul>
    <li>🌿 <b>Spring Boot</b> (Web, Data JPA)</li>
    <li>🐘 <b>PostgreSQL</b></li>
    <li>🌱 <b>JPA: Hibernate</b></li>
    <li>📜 <b>Liquibase</b> (Migração de banco de dados)</li>
    <li>🎭 <b>Lombok</b></li>
    <li>✨ <b>REST</b></li>
    <li>✨ <b>DDD</b></li>
</ul>

<hr/>

<h2>🟢 Como Rodar o Projeto</h2>

<h3>🔨 1. Construir o Projeto</h3>

<p>Para compilar e baixar todas as dependências, execute:</p>

<pre><code class="sh">
mvn clean install
</code></pre>

<h3>▶️ 2. Iniciar a Aplicação</h3>

<p>Agora, para rodar o projeto, utilize o comando:</p>

<pre><code class="sh">
mvn spring-boot:run
</code></pre>

<p>📌 <b>A aplicação estará disponível em:</b></p>
<a href="http://localhost:8080">http://localhost:8080</a> (ou na porta configurada no <code>application.properties</code>)

<hr/>

<h3>🛑 Parar a Aplicação</h3>

<p>Para interromper a execução, pressione:</p>

<pre><code class="sh">
CTRL + C
</code></pre>

<hr/>
