package com.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import handyman.HandyManUtils;

public class Constantes {
    public static final int PAGINATION_LIMIT=5;
    public static final int SESSION_EXPIRE=30;

    public static final String USER_TABLE="piedpiper_users";

    public static final int ADMIN_LEVEL=10;
    public static final int USER_LEVEL=5;
    public static final int GUEST_LEVEL=0;

    public static final int ADMIN_ID=1;
    public static final int USER_ID=2;
    public static final int GUEST_ID=0;

    public static final String UNAUTHENTICATED_MESSAGE=HandyManUtils.encodeURL_UTF8("Utilisateur non authentifié");
    public static final String UNAUTHORIZED_MESSAGE=HandyManUtils.encodeURL_UTF8("Accès non authorisé");

    public static final LinkedList<String> PHOTO_EXTENSIONS=new LinkedList<>(Arrays.asList(".jpeg", ".jpg", ".JPEG", ".JPG", ".png", ".PNG", ".bmp", ".BMP"));
    public static final String PHOTO_EXTENSIONS_HTML=".jpeg, .jpg, .JPEG, .JPG, .png, .PNG, .bmp, .BMP";

    public static final String PHONE_REGEXP="^0(20|3[2348])\\s\\d{2}\\s\\d{3}\\s\\d{2}$";
    public static final String EMAIL_REGEXP="^[\\w-\\.]+@[\\w-]+\\.[\\w-]{2,}$";

    public static final String ACCESS_IMAGES="D:\\Eriq_RohWeltall\\Assigns\\Eval\\PiedPiper\\Access\\Images";

    public static final String PASSWORD_SALT=">?►ä<ì?f??|???ç?o?üX?îgtNuZ?²ù?‼♫A9??E????L?m1ê? V??d☻§q?,A?¿ªkY{Æ??ò?bK↓çR⌂ô?çx ♦?,?ÆpnàYUc";

    public static final String DEVIS_PREFIXE="DEV";
    public static final int DEVIS_PADDING=6;

    public static final int ANNEE_DEFAUT=2024;
    public static final int STATISTIQUES_NBANNEES_DEFAUT=5;

    public static final HashMap<Integer, String> MOIS=new HashMap<>(){{
        put(1, "JAN");
        put(2, "FEB");
        put(3, "MAR");
        put(4, "APR");
        put(5, "MAY");
        put(6, "JUN");
        put(7, "JUL");
        put(8, "AUG");
        put(9, "SEP");
        put(10, "OCT");
        put(11, "NOV");
        put(12, "DEC");
    }};

    public static final String QUERY_DEVIS_MOIS="""
        select mois.numero as moisnumero, mois.nom as moisstring, coalesce(temp.montant_total, 0) as montant_total from mois 
        left join (select mois, montant_total from v_devis_total_mois where annee=%s) as temp on mois.numero=temp.mois
        """;

    public static final String ARIARY="Ar";
    public static final String RESET_QUERY="""
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
            """;
        
        public static final String PAIEMENT_PREFIX="PAI";
        public static final int PAIEMENT_PADDING=6;

        public static final String[] DATEFORMATS={"d/M/yyyy", "d/MM/yyyy", "dd/M/yyyy", "dd/MM/yyyy"};

        public static final String CLEAN_IMPORT_TABLES_PAIEMENTS="""
            delete from import_paiements;
            """;
        public static final String CLEAN_IMPORT_TABLES_MAISON="""
            delete from import_maisontravaux;
            """;
        public static final String CLEAN_IMPORT_TABLES_DEVIS="""
            delete from import_devis;
            """;
}