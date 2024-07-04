insert into utilisateurs values(default, '032 63 156 28');

insert into type_maisons values(default, 'traditionnelle', 26),
                               (default, 'contemporaine', 35),
                               (default, 'moderne', 66.9);

INSERT INTO description_maisons VALUES 
    ( DEFAULT, 1, '2 Chambres' ),
    ( DEFAULT, 1, '1 Toilette' ),
    ( DEFAULT, 1, '1 Cuisine' );

INSERT INTO description_maisons VALUES 
    ( DEFAULT, 2, '3 Chambres' ),
    ( DEFAULT, 2, '1 Toilette' ),
    ( DEFAULT, 2, '1 Cuisine' ),
    ( DEFAULT, 2, '1 Roof-top' );

INSERT INTO description_maisons VALUES 
    ( DEFAULT, 3, '2 Etages' ),
    ( DEFAULT, 3, '4 Chambres' ),
    ( DEFAULT, 3, '2 Toilette' ),
    ( DEFAULT, 3, '2 Cuisine' ),
    ( DEFAULT, 3, '1 Roof-top' );

insert into type_finitions values(default, 'Standard', 0),
                               (default, 'Gold', 25),
                               (default, 'Premium', 35.6),
                               (default, 'VIP', 45.86);

insert into unites values(default, 'm2'),
                         (default, 'm3');
insert into unites values(default, 'fft');                         

-- insert into travaux values(default, 'mur de soutenement et demi Cloture ht 1m', 2, 190000, '001'),
--                           (default, 'Decapage des terrains meubles', 1, 3072.87, '101'),
--                           (default, 'Dressage du plateforme', 1, 3736.26, '102');

-- INSERT INTO travaux VALUES 
--     ( DEFAULT, 'mur de soutenement et demi Cloture ht 1m', 1, 190000.00, '001' ),
--     ( DEFAULT, 'Décapage des terrains meubles', 2, 3072.87, '101' ),
--     ( DEFAULT, 'Dressage du plateforme', 2, 3736.26, '102' ),
--     ( DEFAULT, 'Fouille d ouvrage terrain ferme', 1, 9390.93, '103' ),
--     ( DEFAULT, 'Remblai d ouvrage', 1, 37563.26, '104' ),
--     ( DEFAULT, 'Travaux d implantation', 3, 152656.00, '105' ),
--     ( DEFAULT, 'maçonnerie de moellons, ep= 35cm', 1, 172114.40, '201' ),
--     ( DEFAULT, 'beton armée dosée à 350kg/m3 semelles isolée', 1, 573215.80, '2021' ),
--     ( DEFAULT, 'beton armée dosée à 350kg/m3 amorces poteaux', 1, 573215.80, '2022' ),
--     ( DEFAULT, 'beton armée dosée à 350kg/m3 chaînage bas', 1, 573215.80, '2023' ),
--     ( DEFAULT, 'remblai technique', 1, 37563.26, '203' ),
--     ( DEFAULT, 'herrissonage ep=10', 1, 73245.40, '204' ),
--     ( DEFAULT, 'beton ordinaire dosée à 300kg/m3 pour form', 1, 487815.80, '205' ),
--     ( DEFAULT, 'chape de 2cm', 1, 33566.54, '206' );

INSERT INTO travaux VALUES 
    ( DEFAULT, 'mur de soutenement et demi Cloture ht 1m', 1, 190000.00, '001' ),
    ( DEFAULT, 'Decapage des terrains meubles', 2, 3072.87, '101' ),
    ( DEFAULT, 'Dressage du plateforme', 2, 3736.26, '102' ),
    ( DEFAULT, 'Fouille d ouvrage terrain ferme', 1, 9390.93, '103' ),
    ( DEFAULT, 'Remblai d ouvrage', 1, 37563.26, '104' ),
    ( DEFAULT, 'Travaux d implantation', 3, 152656.00, '105' ),
    ( DEFAULT, 'maconnerie de moellons, ep= 35cm', 1, 172114.40, '201' ),
    ( DEFAULT, 'beton armee dosee a 350kg/m3 semelles isolee', 1, 573215.80, '2021' ),
    ( DEFAULT, 'beton armee dosee a 350kg/m3 amorces poteaux', 1, 573215.80, '2022' ),
    ( DEFAULT, 'beton armee dosee a 350kg/m3 chainage bas', 1, 573215.80, '2023' ),
    ( DEFAULT, 'remblai technique', 1, 37563.26, '203' ),
    ( DEFAULT, 'herrissonage ep=10', 1, 73245.40, '204' ),
    ( DEFAULT, 'beton ordinaire dosee a 300kg/m3 pour form', 1, 487815.80, '205' ),
    ( DEFAULT, 'chape de 2cm', 1, 33566.54, '206' );

-- Traditionnelle
INSERT INTO travaux_typemaisons VALUES
( DEFAULT, 1, 1, 26.8),
( DEFAULT, 1, 2, 101.36),
( DEFAULT, 1, 3, 101.36),
( DEFAULT, 1, 4, 24.44),
( DEFAULT, 1, 5, 15.59);


-- Contemporaine
INSERT INTO travaux_typemaisons VALUES
( DEFAULT, 2, 1, 26.8),
( DEFAULT, 2, 2, 101.36),
( DEFAULT, 2, 3, 101.36),
( DEFAULT, 2, 4, 24.44),
( DEFAULT, 2, 5, 15.59),

( DEFAULT, 2, 6, 1 ),
( DEFAULT, 2, 7, 9.62 ),
( DEFAULT, 2, 8, 0.53 ),
( DEFAULT, 2, 9, 0.56 ),
( DEFAULT, 2, 10,2.44 );


-- Moderne
INSERT INTO travaux_typemaisons VALUES
( DEFAULT, 3, 1, 26.8),
( DEFAULT, 3, 2, 101.36),
( DEFAULT, 3, 3, 101.36),
( DEFAULT, 3, 4, 24.44),
( DEFAULT, 3, 5, 15.59),
( DEFAULT, 3, 6, 1 ),
( DEFAULT, 3, 7, 9.62 ),
( DEFAULT, 3, 8, 0.53 ),
( DEFAULT, 3, 9, 0.56 ),
( DEFAULT, 3, 10,2.44 ),

( DEFAULT, 3, 11, 15.59 ),
( DEFAULT, 3, 12, 7.80 ),
( DEFAULT, 3, 13, 5.46 ),
( DEFAULT, 3, 14,77.97 );


insert into mois values(default, 1, 'JAN'),
                       (default, 2, 'FEB'),
                       (default, 3, 'MAR'),
                       (default, 4, 'APR'),
                       (default, 5, 'MAY'),
                       (default, 6, 'JUN'),
                       (default, 7, 'JUL'),
                       (default, 8, 'AUG'),
                       (default, 9, 'SEP'),
                       (default, 10, 'OCT'),
                       (default, 11, 'NOV'),
                       (default, 12, 'DEC');