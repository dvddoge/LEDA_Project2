# Projeto EDA - Ligas Europeias

## Desenvolvedores:
- Deyvid Jerônimo de Araújo Macêdo
- Kaio Emanuel Rosemiro de Carvalho

Este projeto é uma extensão do projeto [LEDA_Project1](https://github.com/zKerc/LEDA_Project1/tree/main). A principal modificação nesta extensão é a substituição dos arrays nativos do Java nos algoritmos de ordenação por três estruturas de dados de nossa escolha, aprimorando o projeto anterior com novas abordagens e técnicas de estrutura de dados.

## Orientações sobre a execução:

* Certifique-se de que o arquivo `matches.csv` esteja localizado na pasta `data`. (caso seja precise baixar o arquivo aqui está o link: https://drive.google.com/drive/folders/1qTCGYDRmoS9-K_etvUUsyWTnca354lPY?usp=sharing)
* Execute o arquivo `Main.java`.
* Através do menu interativo, selecione a coluna desejada para ordenação (opções: attendance, date, venue).
* Escolha o algoritmo de ordenação que deseja utilizar (opções disponíveis nas subpastas do diretório `Ordenacao`).
* Após a seleção do algoritmo, os arquivos ordenados serão gerados automaticamente, sendo o caso médio identificado com o sufixo`_medioCaso`, o melhor caso com o sufixo `_melhorCaso`, e o pior caso com o sufixo `_piorCaso`.
* Se optar pela ordenação com QuickSort, os arquivos serão gerados em pastas separadas dentro da pasta `Ordenacao`, tendo uma pasta específica para o `QuickSort` e outra para o `QuickSortMediana3`.
* Ao final do processo, o tempo para execução da ordenação será exibido no terminal.
* O output também mostrará os caminhos onde cada arquivo foi gerado, facilitando sua localização.

O projeto foi estruturado de forma clara e intuitiva, simplificando o processo de execução e análise. Caso tenha dúvidas ou sugestões, estamos à disposição para esclarecimentos.
