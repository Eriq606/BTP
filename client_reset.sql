alter table paiements drop constraint paiements_iddevis_fkey;
alter table devis drop constraint devis_idtypemaison_fkey;
alter table devis drop constraint devis_idtypefinition_fkey;
alter table devis drop constraint devis_idclient_fkey;
alter table historique_travaux_devis drop constraint historique_travaux_devis_iddevis_fkey;
alter table historique_details_devis drop constraint historique_details_devis_iddevis_fkey;
alter table travaux_typemaisons drop constraint travaux_typemaisons_idtypemaison_fkey;
alter table travaux_typemaisons drop constraint travaux_typemaisons_idtravaux_fkey;
alter table travaux drop constraint travaux_idunite_fkey;
alter table paiements drop constraint paiements_idutilisateur_fkey;
alter table description_maisons drop constraint description_maisons_idtypemaison_fkey;
truncate paiements restart identity;
truncate utilisateurs restart identity;
truncate historique_details_devis restart identity;
truncate historique_travaux_devis restart identity;
truncate devis restart identity;
truncate travaux_typemaisons restart identity;
truncate travaux restart identity;
truncate type_finitions restart identity;
truncate type_maisons restart identity;
truncate description_maisons restart identity;
truncate import_maisontravaux restart identity;
truncate import_paiements restart identity;
truncate import_devis restart identity; 
truncate unites restart identity;
alter table paiements add constraint paiements_iddevis_fkey foreign key(iddevis) references devis(id);
alter table devis add constraint devis_idtypemaison_fkey foreign key(idtypemaison) references type_maisons(id);
alter table devis add constraint devis_idtypefinition_fkey foreign key(idtypefinition) references type_finitions(id);
alter table travaux_typemaisons add constraint travaux_typemaisons_idtypemaison_fkey foreign key(idtypemaison) references type_maisons(id);
alter table travaux_typemaisons add constraint travaux_typemaisons_idtravaux_fkey foreign key(idtravaux) references travaux(id);
alter table travaux add constraint travaux_idunite_fkey foreign key(idunite) references unites(id);
alter table historique_travaux_devis add constraint historique_travaux_devis_iddevis_fkey foreign key(iddevis) references devis(id);
alter table historique_details_devis add constraint historique_details_devis_iddevis_fkey foreign key(iddevis) references devis(id);
alter table paiements add constraint paiements_idutilisateur_fkey foreign key(idutilisateur) references utilisateurs(id);
alter table description_maisons add constraint description_maisons_idtypemaison_fkey foreign key(idtypemaison) references type_maisons(id);
alter table devis add constraint devis_idclient_fkey foreign key(idclient) references utilisateurs(id);


\i D:/Eriq_RohWeltall/Assigns/Eval/BTP/client_data.sql