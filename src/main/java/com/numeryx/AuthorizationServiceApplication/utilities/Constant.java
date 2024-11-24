package com.numeryx.AuthorizationServiceApplication.utilities;

public class Constant {

    private Constant() {
    }

    // ERROR MESSAGES
    public static final String INVALID_EMAIL_USER_EXCEPTION = "Email invalide";
    public static final String REQUIRED_EMAIL = "Email est obligatoire";
    public static final String USERNAME_ALREADY_EXIST_EXCEPTION = "E-mail existe déjà";
    public static final String USER_ALREADY_EXIST_WITH_FISCAL_NUMBER_EXCEPTION = "Utilisateur existe déjà avec le matricule fiscal suivant : ";
    public static final String USER_ALREADY_EXIST_WITH_CIN_EXCEPTION = "Utilisateur existe déjà avec la CIN suivante : ";
    public static final String USERNAME_DOUBLING_IN_LIST_EXIST_EXCEPTION = "E-mail en double dans la liste des utilisateurs à créer";
    public static final String USERNAME_DOES_NOT_EXIST_EXCEPTION = "Compte inexistant";
    public static final String IDENTIFICATION_ALREADY_EXIST_EXCEPTION = "Identifiant existe déjà ";
    public static final String IDENTIFICATION_DOUBLING_IN_LIST_EXIST_EXCEPTION = "Identifiant en double dans la liste des utilisateurs à créer";
    public static final String USER_NOT_FOUND_EXCEPTION = "Utilisateur non trouvé";
    public static final String WRONG_PASSWORD_EXCEPTION = "Mot de passe erronée";
    public static final String WRONG_VALIDATION_CODE_EXCEPTION = "Code de validation erroné";
    public static final String WRONG_TOKEN_EXCEPTION = "Token erroné";
    public static final String USER_WITH_FISCAL_NUMBER_EXCEPTION = "Veuillez bien se connecter avec votre Matricule Fiscale";
    public static final String UNAUTHORIZED_ACTION_EXCEPTION = "Vous n'êtes pas autorisé à effectuer cette action. Veuillez contacter votre administrateur";
    public static final String WEAK_PASSWORD_EXCEPTION = "Faible mot de passe";
    public static final String ROLE_USER_NOT_EXIST_EXCEPTION = "Role utilisateur n'existe pas";
    public static final String FAILED_CREATED_COMPTE_EXCEPTION = "Votre demande de création du compte a échoué";
//    public static final String USER_ACCOUNT_BLOCKED_EXCEPTION = "Votre compte à été bloqué, veuillez contacter l'administrateur ";

    public static final String TOKEN_EXPIRED = "Token expiré";
    public static final String VALIDATION_CODE_EXPIRED = "Code de validation expiré. Veuillez demander un nouveau code en cliquant sur 'Renvoyer un nouveau code'";
    public static final String VALIDATION_TOKEN_EXPIRED = "Invalid token";
    public static final int IDENTIFICATION_CIN_INPUT_SIZE = 8;
    public static final int IDENTIFICATION_FISCAL_NUMBER_INPUT_SIZE = 8;
    public static final int PHONE_INPUT_SIZE = 8;
    public static final int NB_RESEND_ACTIVATION_CODE_ATTEMPT = 3;
    public static final int NB_FAILED_ATTEMPT_ACTIVATION_CODE = 3;


    // TODO: 17‏/9‏/2020 Add text to string validation
    public static String EMAIL_EMPTY = "Email est vide";
    public static String USER_FIRST_NAME_EMPTY = "Nom est vide";
    public static String USER_LAST_NAME_EMPTY = "Prénom est vide";
    public static String JOB_EMPTY = "La fonction est vide";
    public static String PHONE_INVALID = "Téléphone est invalide";
    public static String ROLE_EMPTY = "Role n'existe pas";
    public static String USER_NAME_EMPTY = "Email est vide";
    public static String PHONE_EMPTY = "Numéro de téléphone mobile est vide";
    public static String USERNAME_ALREADY_EXIST = "L'email existe déjà";
    public static String WRONG_FUNCTION = "Fonction n'existe pas";
    public static final String SUBSCRIBER_NOT_FOUND_EXCEPTION = "Abonné non trouvé";
    public static final String USER_ALREADY_EXISTS = "L'email entrée existe déjà pour un autre utilisateur";
    public static final String USER_ALREADY_EXISTS_FOR_SUBSCRIBER = "Un utilisateur avec le même email existe déjà pour l'abonné";
    public static final String NULL_SUBSCRIBER_NAME = "Le nom de l'abonné est obligatoire";
    public static final String NULL_SUBSCRIBER_ADDRESS = "Le'adresse de l'abonné est obligatoire";
    public static final String NULL_SUBSCRIBER_SIRET = "SIRET de l'abonné est obligatoire";
    public static final String NULL_SUBSCRIBER_NAFCODE = "Le code NAF de l'abonné est obligatoire";
    public static final String NULL_SUBSCRIBER_CITY = "La ville de l'abonné est obligatoire";
    public static final String NULL_SUBSCRIBER_MAIN_ADDRESS = "L'adresse de l'abonné est obligatoire";
    public static final String NULL_SUBSCRIBER_COMPLEMENTARY_ADDRESS = "L'adresse complémentaire de l'abonné est obligatoire";
    public static final String NULL_SUBSCRIBER_ZIP = "Le code postal de l'abonné est obligatoire";
    public static final String EMPTY_SUBSCRIBER_NAME = "Le nom de l'abonné est vide";
    public static final String EMPTY_SUBSCRIBER_SIRET = "SIRET de l'abonné est vide";
    public static final String EMPTY_SUBSCRIBER_NAFCODE = "Le code NAF de l'abonné est vide";
    public static final String EMPTY_SUBSCRIBER_CITY = "La ville de l'abonné est vide";
    public static final String EMPTY_SUBSCRIBER_MAIN_ADDRESS = "L'adresse de l'abonné est vide";
    public static final String EMPTY_SUBSCRIBER_COMPLEMENTARY_ADDRESS = "L'adresse complémentaire de l'abonné est vide";
    public static final String EMPTY_SUBSCRIBER_ZIP = "Le code postal de l'abonné est vide";
    public static final String INVALID_ZIP = "Le code postal est invalide";
    public static final String SUBSCRIBER_ALREADY_EXISTS = "L'abonné existe déjà";
    public static final String SUBSCRIBER_DOES_NOT_EXISTS = "L'abonné n'existe pas";
    public static final String ADDRESS_DOES_NOT_EXISTS = "L'adresse n'existe pas pour l'abonné en question";
    public static final String ID_IS_NULL = "Id obligatoire";


    // REGEX
    public static String emailRegex = "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
    public static String phoneRegex = "^[0-9]*$";
    public static String simpleUserPasswordRegex = "^(?=.*[A-Z])(?=.*[\\[\\]é\"'{}()|!@#$&*_çà=/+-])(?=.*[0-9])(?=.*[a-z]).{8,50}$";

}
