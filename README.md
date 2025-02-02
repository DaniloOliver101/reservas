Requisitos do Projeto
Objetivo Principal
Desenvolver uma API RESTful para:
• Registrar reservas de mesas.
• Controlar o status das reservas e das mesas.
• Bloquear reservas quando a mesa estiver ocupada.
Stack Recomendado
• Backend: Java (Spring).
• Banco de Dados:  PostgreSQL
• Autenticação: JWT (JSON Web Tokens)
 
Funcionalidades
1. 
Autenticação de Usuário
• Registro: O usuário deve ser capaz de se registrar com um nome, e-mail e senha.
• Login: Usuários autenticados recebem um token JWT para acesso às funcionalidades de reservas.
• Restrição de Acesso: Apenas usuários logados podem criar e visualizar reservas.
2. 
Gestão de Mesas
• Listagem: Listar todas as mesas disponíveis no restaurante.
• Criar Mesa: Administradores podem adicionar novas mesas ao sistema com um nome e capacidade de pessoas.
• Status da Mesa: Cada mesa pode estar disponível, reservada ou inativa.
3. 
Sistema de Reservas
• Criar Reserva: Usuários autenticados podem criar reservas para mesas específicas.
• Verificar Disponibilidade: A API deve verificar se a mesa está disponível no horário solicitado antes de confirmar a reserva.
• Cancelar Reserva: Usuários podem cancelar suas reservas, o que libera a mesa para novas reservas.
4. 
Controle de Status
• Status das Mesas: Mesas ficam reservadas automaticamente ao serem associadas a uma reserva.
• Status das Reservas: Reservas têm status ativo quando confirmadas e cancelado quando canceladas.
 
Estrutura do Banco de Dados
• 
Usuários
• id: Identificador único.
• nome: Nome do usuário.
• email: E-mail do usuário (único).
• senha: Senha do usuário, armazenada com hash.
• role: Papel do usuário (ex.: cliente ou administrador).
• 
Mesas
• id: Identificador único.
• nome: Nome ou número da mesa.
• capacidade: Quantidade máxima de pessoas que a mesa comporta.
• status: Status atual da mesa (disponível, reservada, inativa).
• 
Reservas
• id: Identificador único.
• usuario_id: ID do usuário que fez a reserva.
• mesa_id: ID da mesa reservada.
• data_reserva: Data e horário da reserva.
• status: Status da reserva (ativo, cancelado).
 
Endpoints da API
Autenticação
• POST /usuarios/registrar — Cadastro de novos usuários.
• POST /usuarios/login — Login de usuários e geração de token JWT.
Mesas
• GET /mesas — Lista todas as mesas e seus status.
• POST /mesas — Adiciona uma nova mesa (apenas administradores).
• PATCH /mesas/:id — Atualiza informações de uma mesa.
• DELETE /mesas/:id — Remove uma mesa (apenas administradores).
Reservas
• POST /reservas — Cria uma nova reserva, validando disponibilidade e a capacidade da mesa.
• GET /reservas — Lista todas as reservas do usuário autenticado.
• PATCH /reservas/:id/cancelar — Cancela uma reserva ativa.
 
Regras de Negócio
1. 
Verificação de Disponibilidade
• Antes de criar uma reserva, verifique se a mesa está disponível no horário desejado.
2. 
Validação de Capacidade
• O sistema deve validar a capacidade da mesa para atender o número de pessoas indicado na reserva.
3. 
Cancelamento de Reserva
• Quando uma reserva é cancelada, o sistema deve atualizar o status da mesa para disponível.
4. 
Permissões de Usuário
• Apenas administradores podem adicionar, atualizar ou remover mesas.
• Apenas o usuário que criou uma reserva pode cancelá-la.
 
Validação de Dados
• Datas e Horários: A reserva só pode ser feita para horários futuros dentro do horário de funcionamento do restaurante.
• Campos Obrigatórios: Valide a presença de todos os campos obrigatórios em cada endpoint.
• Formatos: E-mails e datas devem estar em formatos corretos.
 
