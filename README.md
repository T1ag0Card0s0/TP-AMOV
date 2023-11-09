# TP-AMOV

## TEMA

Uma aplicação Android que facilite as visitas
turísticas a diversas localizações, disponibilizando informação sobre os locais de interesse existentes
nesses locais.

---

## FUNCIONALIDADES

- Permitir selecionar o local pretendido entre uma lista de localizações.

- Ordenar localizações:

  - Ordem alfabetica
  - Distancia ao local o utilizador se encontra

- Depois de selecionado o local pode ordenar por:

  - Categorias
  - Ordem alfabetica dos nomes
  - Distancia

- Mostrar num mapa os locais de interesse de uma determinada zona

- Contribuição por parte dos utilizadores para a informação existente:

  - Adicionar uma nova localização (cidade ou outra)
  - Adicionar uma nova categoria
  - Adicionar um novo local de interesse

- Para criar uma nova categoria é exigido:

  - Indicação de uma imagem representativa (eventualmente um icone)
  - Uma frase simples

- Para criar um local de interesse:

  - Deverá ser associado uma localização
  - Deverá ser associado uma categoria
    (Opcional - Associar locais de interesse a diferentes localizações e/ou diferentes categorias)

- Para criar um local de interesse ou uma localização devem ser adicionadas coordenadas geograficas:

  - Preferencialmente obtidas pela localização do dispositivo usado.
  - Podem ser indicadas por escrita direta

- Para mostrar as coordenadas de uma localização ou local de interesse:

  - Tem de ser especificado como foram obtidas:
  - Se foram obtidas pela localização do dispositivo tem de ser mostrado o nivel de precisão (podendo usar texto, icones ou outras formas de representar a precisão)

- Cada contribuição só será aceite para divulgação generalizada quando dois utilizadores aprovarem a informação.

- Mostrar na aplicação elementos que estão em fase de aprovação (os utilizadores podem consultar a informação, mas devem ser avisados que a informação pode nao ser de confiança).

- Criar uma forma que permita validar a informação por outro utilizador.

- Editar/Alterar dados introduzidos (Apenas pelo utilizador que criou a informação):

  - Os elementos editados/alterados ficam sujeitos a uma nova fase de aprovação.

- Classificar locais de interesse (Opcional edição de classificações):

  - Numa escala de 0 a 3, os utilizadores só podem votar uma vez.
  - Pode-se adicionar apenas uma foto
  - Pode-se adicionar apenas um comentario com no máximo 200 caracteres.

- Eliminar locais de interesse (apenas pelos seus autores):

  - "Mas depois de possuirem votações só o poderão ser com a aprovação de remoção por 3 utilizadores."

- Eliminar categorias e localizações (Apenas pelos seus autores):

  - As categorias e localizações não podem possuir local de interesse

- Registo e autenticação de um utilizador.

---

## Estruturas e dados

- Localização: - Cidade - Serra - Praia - Ilhas - Regiões
  (etc)
- Categorias(base): - Museus - Monumentos&Locais de culto - Jardins - Miradouros - Restaurantes&Bares - Alojamento(incluir hoteis e outras formas de alojamento)
  (Podem ser definidas novas categorias pelos utilizadores)
- Cada localização ou local de interesse deve possuir:
  - Descrição
  - Fotografia
  - Latitude
  - Longitude

---

## Desenvolvimento

- Utilizar: Jetpack Compose, sendo admissível a utilização de layouts XML em alguns ecrãs.
- Aconselhado o uso de Firebase para facilitar a partilha de informação entre os utilizadores.
- Utilizar mecanismos de autenticação para garantir o registo das ações dos utilizadores (adicionar novos elementos,comentarios fotografias ou classificaçoes)
- O armazenamento pode ser feito através: Firestone, Realtime Database ou Storage, individualmente ou em conjunto.
- A app tem de ter um registo e autenticação de um utilizador.

---

## Parâmetros sujeitos a avaliação

- Interface e interação com o utilizador
- Pesquisas, filtros, ordenamentos e visualização da informação
- Utilização de mapas para mostrar informação georreferenciada
- Criação e gestão de locais
- Criação e gestão de categorias
- Criação e gestão de locais de interesse
- Processo de confirmação de nova informação
- Processo de remoção de informações
- Armazenamento e consulta dos dados a partir de um servidor/repositório partilhado
- Registo e autenticação de utilizadores
- Suporte para diferentes línguas (mínimo inglês e outra língua; este parâmetro refere-se às mensagens mostradas pela própria aplicação e não as referentes às informações geridas pela aplicação)
- Suporte para diferentes orientações de ecrã
- Ecrã de créditos (alunos, disciplina, ano letivo, curso)
- Robustez e qualidade do código (inclui organização, uso de padrões, tratamento de erros, exceções)
- Relatório Técnico
- Manual do Utilizador

---

## Entrega

- Prazo: 08:00 do dia 2 de Janeiro de 2024
- Formato do ficheiro zip: AMOV.2023.2024.<nr_aluno1>.<nr_aluno2>.<nr_aluno3>.zip
- O ficheiro zip tem de incluir: - todo o código (pastas com os projetos) com todos os recursos essenciais para a compilação e execução. Nos projetos do Android Studio deverão ser removidos previamente os diretórios: <proj>/.gradle e <proj>/app/build
- Caso os ficheiros referidos não sejam removidos será aplicada uma penalização de 5% na nota final
- Relatório técnico (PDF)
- Manual do utilizador (PDF)
