INSERT INTO restaurants (id, nome, ativo, street, number, district, complement, city, state, zip_code) VALUES
    ('a1b2c3d4-0001-0001-0001-000000000001', 'Pizzaria Bella Napoli',      true, 'Rua Augusta',               '1500', 'Consolação',      'Loja 1',    'São Paulo',       'SP', '01305-100'),
    ('a1b2c3d4-0001-0001-0001-000000000002', 'Sushi Garden',               true, 'Av. Paulista',              '900',  'Bela Vista',      NULL,        'São Paulo',       'SP', '01310-200'),
    ('a1b2c3d4-0001-0001-0001-000000000003', 'Churrascaria Gaúcha',        true, 'Rua das Flores',            '200',  'Centro',          NULL,        'Porto Alegre',    'RS', '90010-050'),
    ('a1b2c3d4-0001-0001-0001-000000000004', 'Cantina do Marco',           true, 'Rua XV de Novembro',        '450',  'Centro Histórico', NULL,       'Curitiba',        'PR', '80020-310'),
    ('a1b2c3d4-0001-0001-0001-000000000005', 'Tapiocaria Nordestina',      true, 'Av. Getúlio Vargas',        '1200', 'Meireles',        'Galeria A', 'Fortaleza',       'CE', '60165-080'),
    ('a1b2c3d4-0001-0001-0001-000000000006', 'Hamburgueria Black Bull',    true, 'Rua Pedro Alvares Cabral',  '75',   'Savassi',         NULL,        'Belo Horizonte',  'MG', '30140-080'),
    ('a1b2c3d4-0001-0001-0001-000000000007', 'Restaurante Mar e Terra',    true, 'Av. Beira Mar',             '3000', 'Miramar',         NULL,        'João Pessoa',     'PB', '58032-100'),
    ('a1b2c3d4-0001-0001-0001-000000000008', 'Comida Caseira da Vovó',     true, 'Rua Dom Pedro II',          '330',  'Boa Viagem',      'Sala 2',    'Recife',          'PE', '51020-220'),
    ('a1b2c3d4-0001-0001-0001-000000000009', 'Vegano & Natural',           true, 'Rua da Consolação',         '2800', 'Higienópolis',    NULL,        'São Paulo',       'SP', '01416-000'),
    ('a1b2c3d4-0001-0001-0001-000000000010', 'Frango Grelhado Express',    true, 'Av. Brasil',                '500',  'Centro',          NULL,        'Rio de Janeiro',  'RJ', '20040-020')
ON CONFLICT (id) DO NOTHING;
