# Chat em Java
Integrantes: 
Ana Carolyne Pereira de Souza
Matheus Fleury Mainardi
Vinícius Honorato Aquino da Silva e Souza

Este é um programa de chat implementado em Java, permitindo a troca de mensagens de texto e imagens entre múltiplos clientes conectados a um servidor central.

## Pré-requisitos

Certifique-se de ter os seguintes requisitos instalados:
- Java Development Kit (JDK)
- Ambiente para compilar e executar arquivos Java

## Como Executar

1. **Clone o Repositório**
   ```bash
   git clone <https://github.com/Mainardi18/ChatJava>
   cd <POOChat>
   ```

2. **Compile os Arquivos**
   - Compile todos os arquivos `.java` do projeto:
     ```bash
     javac *.java
     ```

3. **Inicie o Servidor**
   - Execute a classe `Server`:
     ```bash
     java Server
     ```
   - O servidor será iniciado na porta `12345` e ficará aguardando conexões dos clientes.

4. **Conecte os Clientes**
   - Execute a classe `Client`:
     ```bash
     java Client
     ```
   - Insira o nome do cliente quando solicitado. Este nome será exibido para os outros participantes do chat.
   - Você pode abrir várias instâncias do cliente para simular múltiplas conexões.
   - A Classe Cliente pode ser executada várias vezes, sendo assim é possível criar vários usuários.

5. **Uso do Chat**
   - **Mensagens de Texto**: Digite sua mensagem no campo de entrada e clique em "Enviar".
   - **Envio de Imagens**: Clique em "Enviar Imagem", selecione o arquivo desejado e confirme.

6. **Encerramento**
   - Feche a janela do cliente para sair do chat. O servidor notificará os outros participantes sobre a saída.

## Funcionalidades

- Troca de mensagens de texto entre clientes conectados ao servidor.
- Envio de imagens no formato `.png`, `.jpg`, `.jpeg` ou `.gif`.
- O servidor mantém um histórico (backlog) de mensagens que é enviado aos novos clientes ao se conectarem.
- O usuário é notificado sempre que outro usuário entra ou sai do servidor. 

## Estrutura do Código

- **Server**: Classe principal que gerencia conexões dos clientes e distribui mensagens.
- **Client**: Interface gráfica para interação do usuário com o chat.
- **Message**: Representa uma mensagem, que pode ser texto ou imagem.
- **Command**: Interface para comandos enviados no chat e backlog das mensagens antigas.
- **Strategies**: Implementação de estratégias para envio de mensagens de texto e imagens.
