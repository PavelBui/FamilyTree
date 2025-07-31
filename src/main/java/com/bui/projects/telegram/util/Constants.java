package com.bui.projects.telegram.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    //Relationship
    public static final Integer FATHER_TYPE = 1;
    public static final Integer MOTHER_TYPE = 2;
    public static final Integer SON_TYPE = 5;
    public static final Integer DAUGHTER_TYPE = 6;
    public static final Integer BROTHER_TYPE = 9;
    public static final Integer SISTER_TYPE = 10;
    public static final Integer HUSBAND_TYPE = 11;
    public static final Integer WIFE_TYPE = 12;

    //Button codes
    public static final String HOME_BUTTON = "go_home";
    public static final String DEFAULT_BUTTON = "go_default";
    public static final String PHOTO_BUTTON_PREFIX = "go_photos_";
    public static final String PHOTO_BACK_BUTTON_PREFIX = "go_person_after_photo_";
    public static final String PERSON_BUTTON_PREFIX = "go_person_";
    public static final String MULTI_PERSON_BUTTON_SUFFIX = "_multi";

    //Commands
    public static final String START_COMMAND = "/start";
    public static final String HOME_COMMAND = "/home";
    public static final String ME_COMMAND = "/me";

    //Files
    public static final String DEFAULT_PHOTO_FILE = "defaultphoto.jpg";
    public static final String START_IMAGE_FILE = "startimage.jpg";
    public static final String TEMP_FILE_PREFIX = "temp_";
    public static final String TEMP_FILE_SUFFIX = "_";

    //Text
    public static final String HOME_TEXT = "Вернуться к себе";
    public static final String FIND_HOME_TEXT = "Найти себя";
    public static final String DEFAULT_TEXT = "Персона по-умолчанию";
    public static final String PHOTO_TEXT = "Все фотографии";
    public static final String INTRO_TEXT = "Нажмите на кнопку \"Найти себя\", чтобы найти себя в семейном древе используя Телеграм идентификатор, или нажмите на кнопку \"Персона по-умолчанию\", чтобы загрузить персону по-умолчанию и начать путешествие по семейному древу.";
    public static final String WELCOME_PERSON_TEXT = "Добро пожаловать, ";
    public static final String WELCOME_UNKNOWN_TEXT = "Добро пожаловать!";

    public record RelationshipGroup(
            String buttonPrefix,
            String list,
            String male,
            String males,
            String female,
            String females
    ) {}

    public static final RelationshipGroup PARENTS = new RelationshipGroup(
            "go_parents_", "Родители", "Папа", "Папы", "Мама", "Мамы"
    );

    public static final RelationshipGroup KIDS = new RelationshipGroup(
            "go_kids_", "Дети", "Сын", "Сыновья", "Дочь", "Дочери"
    );

    public static final RelationshipGroup SIBLINGS = new RelationshipGroup(
            "go_siblings_", "Братья и сестры", "Брат", "Братья", "Сестра", "Сестры"
    );

    public static final RelationshipGroup SPOUSES = new RelationshipGroup(
            "go_spouses_", "Супруги", "Муж", "Мужья", "Жена", "Жены"
    );
}
