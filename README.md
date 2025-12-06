# Obrigado por instalar o Biller™!

## Sobre
O Biller™ é um programa voltado para empresas que precisam gerar comandas
(ou Bills™) para seus clientes a partir de uma base de dados contendo os 
produtos e taxas que podem ser adicionados.

## Funcionamento
O programa pode ser acessado por duas formas diferentes, ou pelo prompt de
comandos, ou por sua interface gráfica. 

A versão do terminal é um _repl_ (read-eval-print-loop), ou seja, ele 
interpreta os comandos do usuário para interagir com os dados guardados.
Essa versão possuí um comando especial "help" que explica melhor as ações
válidas e as suas respectivas sintaxes.

Já a versão com interface gráfica possuí 6 janelas principais:
1. Menu: é onde ficam os botões para abrir os Managers™.
2. Product Manager™: é a janela onde o usuário pode cadastrar os produtos.
3. Fee Manager™: para cadastro das taxas.
4. Client Manager™: para cadastrar os clientes.
5. Bill Manager™: serve para criar as comandas e acessá-las para edição.
6. Bill Editor™: é a tela de edição das comandas, permitindo a adição dos itens.

## Como rodar o Biller™
Além de você poder compilar o código você mesmo, também estão disponíveis
arquivos _.jar_ prontos para uso!

Basta apenas usar um dos seguintes comandos no seu terminal dependendo
da versão do Biller™ que você deseja utilizar:

Para rodar o programa com interface gráfica, use o comando:
```bash
java -jar ./path/to/Biller.jar --gui
```
Ou então se preferir usar a versão do terminal, rode o comando:
```bash
java -jar ./path/to/Biller.jar --terminal
```

## Salvamento dos dados
Todos os dados salvos ficaram armazenados na pasta 'data' em um
formato de arquivo incompatível com outros programas e altamente
suscetível a corrompimento caso uma nova versão seja disponibilizada.

No entanto, se você desejar um melhor salvamento de dados, o pacote premium acompanha
também o Biller Data Pro™ com mais opções de serialização e armazenamento.
