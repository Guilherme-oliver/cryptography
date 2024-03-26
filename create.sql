create table tb_client (id bigserial not null, result_value bigint, credit_card_token varchar(255), user_document varchar(255), primary key (id));
INSERT INTO tb_client(user_document, credit_card_token, result_value) VALUES ('MzYxNDA3ODE4MzM=', 'YWJjMTIz', 5999);
INSERT INTO tb_client(user_document, credit_card_token, result_value) VALUES ('MzI5NDU0MTA1ODM=', 'eHl6NDU2', 1000);
INSERT INTO tb_client(user_document, credit_card_token, result_value) VALUES ('NzYwNzc0NTIzODY=', 'Nzg5eHB0bw==', 1500);