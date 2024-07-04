drop database btp;
create database btp owner eriq;
-- J1 EVAL
create domain phone as varchar check(value ~ '^[+]?[0-9\-\s]{10,}$');
create domain monnaie as numeric(16,2) check(value >= 0);
create domain pourcent as numeric(16,2) check(value >= 0 and value <= 100);
create domain duree as numeric(16,2) check(value >= 0);
create domain quantite as numeric(16,2) check(value >= 0);
create domain surface as numeric(16,2) check(value > 0);

create table utilisateurs(
    id serial primary key,
    nom varchar not null,
    telephone phone not null,
    motdepasse text not null
);

alter table utilisateurs add constraint user_unique unique(telephone, motdepasse);

create table type_maisons(
    id serial primary key,
    nom varchar not null unique
);
create table type_finitions(
    id serial primary key,
    nom varchar not null unique
);
create table prix_typemaisons(
    id serial primary key,
    idtypemaison int not null references type_maisons(id),
    prix monnaie not null,
    dateheure timestamp default now()
);
create table duree_typemaisons(
    id serial primary key,
    idtypemaison int not null references type_maisons(id),
    dureeheure duree not null
);
create table prix_typefinitions(
    id serial primary key,
    idtypefinition int not null references type_finitions(id),
    pourcentage pourcent not null,
    dateheure timestamp default now()
);
create table devis(
    id serial primary key,
    idtypemaison int not null references type_maisons(id),
    idtypefinition int not null references type_finitions(id),
    "description" varchar not null,
    idmatricule varchar not null unique,
    montant monnaie not null,
    datedebut timestamp not null,
    datefin timestamp not null
);
create table paiements(
    id serial primary key,
    idutilisateur int not null references utilisateurs(id),
    iddevis int not null references devis(id),
    dateheure timestamp not null,
    montant monnaie not null
);

alter table duree_typemaisons add dateheure timestamp not null;
alter table duree_typemaisons drop column dateheure;
alter table duree_typemaisons add dateheure timestamp default now();

-- AJUSTEMENT
alter table utilisateurs drop constraint user_unique;
alter table utilisateurs drop column nom;
alter table utilisateurs drop column motdepasse;

alter table type_maisons add photo text;
alter table type_maisons add "description" text;
alter table type_maisons add dureejours duree default 0;

drop table prix_typemaisons;
drop table duree_typemaisons;
drop table prix_typefinitions;

alter table type_finitions add pourcentage pourcent default 0;

alter table devis add datecreation timestamp default now();
alter table devis drop column datedebut;
alter table devis drop column datefin;
alter table devis add datedebuttravaux date not null;
alter table devis add datefintravaux date not null;

alter table devis add constraint debut_avant_fin check(datedebuttravaux <= datefintravaux);

create table unites(
    id serial primary key,
    symbole varchar not null
);
create table travaux(
    id serial primary key,
    designation varchar not null unique,
    idunite int not null references unites(id),
    prix_unitaire monnaie not null
);
create table travaux_typemaisons(
    id serial primary key,
    idtypemaison int not null references type_maisons(id),
    idtravaux int not null references travaux(id),
    quantiteTravaux quantite not null
);

alter table travaux add numero varchar not null;

create view v_type_maison_prix as
select tm.*, sum(ttm.quantiteTravaux*t.prix_unitaire) as prix 
from type_maisons tm
join travaux_typemaisons ttm on tm.id=ttm.idtypemaison
join travaux t on ttm.idtravaux=t.id
group by tm.id, tm.nom, tm.dureejours;

alter table devis add pourcentagefinition pourcent not null;

create table historique_travaux_devis(
    id serial primary key,
    iddevis int not null references devis(id),
    numerotravaux varchar not null,
    nomtravaux varchar not null,
    unite varchar not null,
    quantitetravaux quantite not null,
    pu monnaie not null,
    montant monnaie
);

create table description_maisons(
    id serial primary key,
    idtypemaison int not null references type_maisons(id),
    valeur text not null
);

drop view v_type_maison_prix;
alter table type_maisons drop column "description";

alter table type_maisons drop column photo;

alter table devis drop column "description";
alter table devis drop column idmatricule;
alter table devis add idmatricule varchar unique;

alter table devis drop column datedebuttravaux;
alter table devis drop column datefintravaux;
alter table devis add datedebuttravaux timestamp;
alter table devis add datefintravaux timestamp;

alter table devis drop column montant;
alter table devis add montant monnaie;

alter table devis add idclient int not null references utilisateurs(id);

create table historique_details_devis(
    id serial primary key,
    iddevis int not null references devis(id),
    nom_typemaison varchar not null,
    nom_typefinition varchar not null,
    dureejours duree not null
);

create view v_travaux_typemaisons as
select ttm.*, t.designation, t.prix_unitaire, t.idunite, t.numero, u.symbole as unite
from travaux_typemaisons ttm
join travaux t on ttm.idtravaux=t.id
join unites u on t.idunite=u.id;

alter table historique_details_devis add numero_client varchar not null;

create table admins(
    id serial primary key,
    identifiant varchar not null,
    motdepasse text not null
);

alter table admins add constraint unique_admin unique(identifiant, motdepasse);

alter table devis add reste_a_payer monnaie default 0;

create view v_devis_mois_annee as
select *, extract(month from datecreation) as mois, extract(year from datecreation) as annee
from devis;

create view v_devis_total_annee as
select annee, sum(montant) as montant_total
from v_devis_mois_annee
group by annee;

create view v_devis_total_mois as
select mois, annee, sum(montant) as montant_total
from v_devis_mois_annee
group by mois, annee;

create table mois(
    id serial primary key,
    numero int not null check(numero > 0 and numero < 13),
    nom varchar not null unique
);

alter table type_maisons add "description" text;
alter table type_maisons add "surface" surface default 1;
alter table devis add lieu varchar not null;
alter table paiements add ref_paiement varchar unique;

create table import_maisontravaux(
    id serial primary key,
    ligne int,
    type_maison varchar,
    "description" text,
    "surface" surface,
    codetravaux varchar,
    type_travaux varchar,
    unite varchar,
    prix_unitaire monnaie,
    "quantite" quantite,
    duree_travaux duree
);

alter table utilisateurs drop column telephone;
alter table utilisateurs add telephone varchar not null unique;

create table import_devis(
    id serial primary key,
    ligne int,
    client varchar,
    ref_devis varchar,
    type_maison varchar,
    finition varchar,
    taux_finition pourcent,
    date_devis timestamp,
    date_debut timestamp,
    lieu varchar
);

create table import_paiements(
    id serial primary key,
    ligne int,
    ref_devis varchar,
    ref_paiement varchar,
    date_paiement timestamp,
    montant monnaie
);

create view v_devis_somme_paye as
select iddevis, sum(montant) as somme
from paiements
group by iddevis;

alter table travaux add constraint unique_codetravaux unique(numero);