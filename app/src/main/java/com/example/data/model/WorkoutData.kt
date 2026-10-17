package com.example.data.model

data class Exercise(
    val id: String,
    val nome: String,
    val series: Int,
    val reps: String,
    val descansoSegundos: Int,
    val nota: String,
    val infoAdicional: String = "",
    val videoUrl: String = ""
)

data class WorkoutDay(
    val diaId: String,
    val diaSemana: String,
    val foco: String,
    val isCoringa: Boolean = false,
    val exercicios: List<Exercise>
)

object WorkoutDataSource {
    const val TREINADOR = "Fahur Assessoria"
    const val ALUNO = "Fahur"
    const val OBJETIVO = "Força funcional, torque mecânico e integridade estrutural"

    val diasTreino: List<WorkoutDay> = listOf(
        WorkoutDay(
            diaId = "coringa",
            diaSemana = "Treino Coringa Express",
            foco = "Corpo Inteiro (Full Body) - Máximo 30 minutos",
            isCoringa = true,
            exercicios = listOf(
                Exercise(
                    id = "cor_1",
                    nome = "Kettlebell Swing (Balanço de Quadril - Explosão)",
                    series = 4,
                    reps = "15",
                    descansoSegundos = 60,
                    nota = "Usa o quadril como alavanca para lançar o peso para frente. Corpo inteiro em alta rotação.",
                    infoAdicional = "Atenção: Não puxe o peso com os ombros. A força vem 100% da explosão de quadril e glúteo.",
                    videoUrl = "https://www.youtube.com/embed/YSxHifyI6s8"
                ),
                Exercise(
                    id = "cor_2",
                    nome = "Thrusters com Halteres (Agachamento + Desenvolvimento)",
                    series = 4,
                    reps = "10",
                    descansoSegundos = 60,
                    nota = "Agacha fundo e usa o impulso da subida para empurrar o peso acima da cabeça sem pausa."
                ),
                Exercise(
                    id = "cor_3",
                    nome = "Flexão de Braço Tradicional ou com Pés Elevados",
                    series = 3,
                    reps = "Até quase a falha",
                    descansoSegundos = 45,
                    nota = "Core travado como uma prancha de madeira, peito encosta no chão.",
                    infoAdicional = "Cotovelos apontando a 45 graus para trás (formato de seta), nunca abertos a 90 graus."
                ),
                Exercise(
                    id = "cor_4",
                    nome = "Remada Curvada com Halteres ou Barra",
                    series = 3,
                    reps = "12",
                    descansoSegundos = 45,
                    nota = "Tronco inclinado a 45 graus, puxar o peso em direção ao quadril espremendo as costas."
                ),
                Exercise(
                    id = "cor_5",
                    nome = "Prancha Abdominal Dinâmica com Toque no Ombro",
                    series = 3,
                    reps = "45 segundos",
                    descansoSegundos = 30,
                    nota = "Em posição de prancha alta, toque o ombro oposto alternadamente sem balançar o quadril.",
                    infoAdicional = "Afaste bem os pés para aumentar a base de apoio e travar a rotação do quadril."
                )
            )
        ),
        WorkoutDay(
            diaId = "segunda",
            diaSemana = "Segunda-feira",
            foco = "Push Global (Empurrar e Estabilização)",
            exercicios = listOf(
                Exercise(
                    id = "seg_1",
                    nome = "Push Press com Barra (Desenvolvimento com Impulso de Perna)",
                    series = 4,
                    reps = "6-8",
                    descansoSegundos = 150,
                    nota = "Explosão leve de quadril para ajudar a empurrar a barra acima da cabeça.",
                    infoAdicional = "Punhos firmes e retos como colunas; encaixe a cabeça à frente assim que a barra passar."
                ),
                Exercise(
                    id = "seg_2",
                    nome = "Supino Reto com Halteres",
                    series = 3,
                    reps = "8-10",
                    descansoSegundos = 90,
                    nota = "Estabilização ativa de ombros, descendo controlado."
                ),
                Exercise(
                    id = "seg_3",
                    nome = "Flexão de Braço com Pés Elevados",
                    series = 3,
                    reps = "Até falha técnica",
                    descansoSegundos = 60,
                    nota = "Core travado o tempo todo, corpo alinhado."
                ),
                Exercise(
                    id = "seg_4",
                    nome = "Tríceps na Polia com Corda",
                    series = 3,
                    reps = "12",
                    descansoSegundos = 60,
                    nota = "Extensão total dos braços abrindo a corda no final do movimento."
                ),
                Exercise(
                    id = "seg_5",
                    nome = "Farmer's Walk (Caminhada do Fazendeiro)",
                    series = 3,
                    reps = "40 metros",
                    descansoSegundos = 90,
                    nota = "Pegada firme, peito estufado e postura ereta.",
                    infoAdicional = "Passos curtos e firmes para evitar que o peso balance lateralmente."
                )
            )
        ),
        WorkoutDay(
            diaId = "terca",
            diaSemana = "Terça-feira",
            foco = "Pull & Cadeia Posterior (Puxar e Lombar)",
            exercicios = listOf(
                Exercise(
                    id = "ter_1",
                    nome = "Levantamento Terra (Convencional ou Trap Bar)",
                    series = 4,
                    reps = "5",
                    descansoSegundos = 240,
                    nota = "Força pura de base, empurrando o chão com as pernas e coluna neutra.",
                    infoAdicional = "Tire a folga da barra antes de arrancar do chão (pré-tensão nas dorsais)."
                ),
                Exercise(
                    id = "ter_2",
                    nome = "Barra Fixa (Puxada Vertical)",
                    series = 3,
                    reps = "Máx controlado",
                    descansoSegundos = 120,
                    nota = "Puxar o peito em direção à barra."
                ),
                Exercise(
                    id = "ter_3",
                    nome = "Remada Cavalinho / T-Bar Row",
                    series = 3,
                    reps = "8-10",
                    descansoSegundos = 90,
                    nota = "Rigidez na lombar, puxando com foco nas dorsais."
                ),
                Exercise(
                    id = "ter_4",
                    nome = "Kettlebell Swing (Balanço de Quadril)",
                    series = 3,
                    reps = "15",
                    descansoSegundos = 60,
                    nota = "Explosão de quadril pura, braços servem apenas de cabo."
                ),
                Exercise(
                    id = "ter_5",
                    nome = "Face Pull na Polia (Puxada Alta no Rostinho)",
                    series = 3,
                    reps = "15",
                    descansoSegundos = 60,
                    nota = "Puxar em direção à testa rotacionando os ombros para trás."
                )
            )
        ),
        WorkoutDay(
            diaId = "quarta",
            diaSemana = "Quarta-feira",
            foco = "Inferiores Unilaterais e Equilíbrio",
            exercicios = listOf(
                Exercise(
                    id = "qua_1",
                    nome = "Agachamento Búlgaro com Halteres",
                    series = 4,
                    reps = "8 por perna",
                    descansoSegundos = 120,
                    nota = "Foco em torque na perna da frente e estabilidade do joelho.",
                    infoAdicional = "Incline levemente o tronco à frente para aumentar o recrutamento do glúteo."
                ),
                Exercise(
                    id = "qua_2",
                    nome = "Terra Romeno Unilateral",
                    series = 3,
                    reps = "10 por perna",
                    descansoSegundos = 90,
                    nota = "Controle de tornozelo e alongamento da posterior da perna de apoio."
                ),
                Exercise(
                    id = "qua_3",
                    nome = "Leg Press Horizontal",
                    series = 3,
                    reps = "10",
                    descansoSegundos = 90,
                    nota = "Empurrar com a sola inteira do pé, descida controlada."
                ),
                Exercise(
                    id = "qua_4",
                    nome = "Panturrilha em Pé no Smith",
                    series = 3,
                    reps = "15",
                    descansoSegundos = 60,
                    nota = "Amplitude total: calcanhar lá embaixo, explosão lá em cima.",
                    infoAdicional = "Pausa de 2 segundos no ponto mais baixo para anular o reflexo elástico do tendão."
                )
            )
        ),
        WorkoutDay(
            diaId = "quinta",
            diaSemana = "Quinta-feira",
            foco = "Core, Potência e Condicionamento Global",
            exercicios = listOf(
                Exercise(
                    id = "qui_1",
                    nome = "Clean and Press com Halteres",
                    series = 4,
                    reps = "6",
                    descansoSegundos = 120,
                    nota = "Integração do chão à cabeça usando impulso do quadril."
                ),
                Exercise(
                    id = "qui_2",
                    nome = "Wall Ball (Arremesso de Bola na Parede)",
                    series = 3,
                    reps = "12",
                    descansoSegundos = 90,
                    nota = "Agachamento profundo ligado direto ao lançamento alto."
                ),
                Exercise(
                    id = "qui_3",
                    nome = "Thrusters com Halteres (Agachamento + Impulso)",
                    series = 3,
                    reps = "10",
                    descansoSegundos = 90,
                    nota = "Motor cardiorrespiratório a 100% sem pausa embaixo."
                ),
                Exercise(
                    id = "qui_4",
                    nome = "Woodchopper na Polia (Movimento de Lenhador)",
                    series = 3,
                    reps = "12 por lado",
                    descansoSegundos = 60,
                    nota = "Rotação de tronco focada nos oblíquos."
                ),
                Exercise(
                    id = "qui_5",
                    nome = "Prancha com Deslocamento de Carga",
                    series = 3,
                    reps = "45 seg",
                    descansoSegundos = 60,
                    nota = "Arraste o peso de um lado para o outro sem deixar o quadril girar."
                )
            )
        ),
        WorkoutDay(
            diaId = "sexta",
            diaSemana = "Sexta-feira",
            foco = "Resistência Mecânica e Padrões Livres",
            exercicios = listOf(
                Exercise(
                    id = "sex_1",
                    nome = "Remada Curvada com Barra",
                    series = 4,
                    reps = "8",
                    descansoSegundos = 120,
                    nota = "Eretores e dorsais firmes, puxando na linha do umbigo."
                ),
                Exercise(
                    id = "sex_2",
                    nome = "Mergulho nas Paralelas (Dips)",
                    series = 3,
                    reps = "10-12",
                    descansoSegundos = 90,
                    nota = "Empurrar vertical com o peso do corpo, tronco levemente inclinado à frente."
                ),
                Exercise(
                    id = "sex_3",
                    nome = "Afundo em Caminhada (Walking Lunges)",
                    series = 3,
                    reps = "20 passos",
                    descansoSegundos = 90,
                    nota = "Passada larga, joelho de trás quase roça no chão."
                ),
                Exercise(
                    id = "sex_4",
                    nome = "Elevação de Joelhos na Barra Fixa",
                    series = 3,
                    reps = "10",
                    descansoSegundos = 60,
                    nota = "Subir os joelhos controlando o balanço do corpo."
                ),
                Exercise(
                    id = "sex_5",
                    nome = "Burpees com Salto",
                    series = 3,
                    reps = "30 segundos",
                    descansoSegundos = 60,
                    nota = "Deita no chão, levanta rápido e salta com braços para cima."
                )
            )
        )
    )
}
