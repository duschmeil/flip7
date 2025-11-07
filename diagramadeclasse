classDiagram
    direction LR

    class Carta {
        <<abstract>>
        - String nome
        - int valor
        + executarAcao(Jogador, Rodada)
        + toString() String
    }

    class CartaNumerica {
        - int numero
        + executarAcao(Jogador, Rodada)
        + toString() String
        + getNumero() int
    }

    class CartaAcao {
        - String efeito
        - String tipo (ACAO/MODIFICADOR)
        + executarAcao(Jogador, Rodada)
        + toString() String
        + getEfeito() String
        + getTipo() String
    }

    class Baralho {
        - List<Carta> cartas
        + Baralho(String)
        + embaralhar()
        + virarCarta() Carta
        + getTamanho() int
    }

    class Jogador {
        - String nome
        - int pontuacaoTotal
        - List<Carta> cartasNaRodada
        - boolean isAtivo
        - boolean bust
        + adicionarCarta(Carta)
        + calcularPontuacaoRodada(boolean) int
        + resetarRodada()
        + stick()
        + checkFlip7() boolean
        + getPontuacaoTotal() int
        + isAtivo() boolean
        + isBust() boolean
    }

    class Rodada {
        - Baralho baralho
        - List<Jogador> jogadores
        - int indiceJogadorAtual
        - boolean rodadaTerminada
        - Jogador jogadorQueFezFlip7
        + Rodada(List<Jogador>, String)
        + iniciarRodada()
        + proximoJogador() Jogador
        + virarCarta(Jogador)
        + executarAcaoFlipThree(Jogador)
        + executarAcaoFreeze()
        + finalizarRodada()
        + isRodadaTerminada() boolean
        + getJogadorAtual() Jogador
        + getJogadores() List<Jogador>
    }

    class Jogo {
        - List<Jogador> jogadores
        - Rodada rodadaAtual
        - String caminhoConfiguracaoBaralho
        + Jogo(List<String>, String)
        + getRodadaAtual() Rodada
        + iniciarJogo(Scanner)
        + verificarVencedor() Jogador
        + salvarEstado()
        + carregarEstado(String) Jogo
    }

    class ExcecaoDeJogo {
        + ExcecaoDeJogo(String)
    }

    Carta <|-- CartaNumerica : herança
    Carta <|-- CartaAcao : herança
    Exception <|-- ExcecaoDeJogo : herança

    Rodada "1" *-- "1" Baralho : usa
    Rodada "1" *-- "N" Jogador : contém
    Jogador "1" *-- "N" Carta : tem
    Jogo "1" *-- "N" Jogador : contém
    Jogo "1" *-- "1" Rodada : gerencia