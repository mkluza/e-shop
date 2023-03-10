# --- !Ups

CREATE TABLE "user_t"
(
    "id"          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "providerId"  VARCHAR NOT NULL,
    "providerKey" VARCHAR NOT NULL,
    "email"       VARCHAR NOT NULL
);


CREATE TABLE "address_t"
(
    "id"          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "providerKey" VARCHAR NOT NULL,
    "firstname"   VARCHAR NOT NULL,
    "lastname"    VARCHAR NOT NULL,
    "city"        VARCHAR NOT NULL,
    "zipcode"     VARCHAR NOT NULL,
    "street"      VARCHAR NOT NULL,
    "phoneNumber" VARCHAR NOT NULL,
    FOREIGN KEY (providerKey) REFERENCES user_t (providerKey)
);

CREATE TABLE "category_t"
(
    "id"   INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "name" VARCHAR NOT NULL
);

CREATE TABLE "product_t"
(
    "id"          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "categoryId"  INTEGER NOT NULL,
    "name"        VARCHAR NOT NULL,
    "description" VARCHAR NOT NULL,
    "price"       VARCHAR NOT NULL,
    "image"       VARCHAR NOT NULL,
    FOREIGN KEY (categoryId) REFERENCES category_t (id)
);

CREATE TABLE "cart_t"
(
    "id"          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "providerKey" VARCHAR NOT NULL,
    "productId"   INTEGER NOT NULL,
    "amount"      INTEGER NOT NULL,
    FOREIGN KEY (providerKey) REFERENCES user_t (providerKey),
    FOREIGN KEY (productId) REFERENCES product_t (id)
);

CREATE TABLE "favourite_product_t"
(
    "id"          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "providerKey" VARCHAR NOT NULL,
    "productId"   INTEGER NOT NULL,
    FOREIGN KEY (providerKey) REFERENCES user_t (providerKey),
    FOREIGN KEY (productId) REFERENCES product_t (id)
);

CREATE TABLE "opinion_t"
(
    "id"          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "providerKey" VARCHAR NOT NULL,
    "productId"   INTEGER NOT NULL,
    "message"     VARCHAR NOT NULL,
    FOREIGN KEY (providerKey) REFERENCES user_t (providerKey),
    FOREIGN KEY (productId) REFERENCES product_t (id)
);


CREATE TABLE "order_t"
(
    "id"        INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "cartId"    INTEGER NOT NULL,
    "addressId" INTEGER NOT NULL,
    FOREIGN KEY (cartId) REFERENCES cart_t (id),
    FOREIGN KEY (addressId) REFERENCES address_t (id)
);


CREATE TABLE "bought_t"
(
    "id"          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "providerKey" VARCHAR NOT NULL,
    "orderId"     INTEGER NOT NULL,
    FOREIGN KEY (providerKey) REFERENCES user_t (providerKey),
    FOREIGN KEY (orderId) REFERENCES order_t (id)
);

INSERT INTO category_t
VALUES (1, 'Pies');
INSERT INTO product_t
VALUES (1, 1, 'Beagle',
        'Te odwa??ne psy o silnej budowie maj?? weso??e usposobienie, s?? zwarte i wysportowane. Ich kr??tka, g??sta i odporna na warunki pogodowe sier???? mo??e mie?? r????ne umaszczenie (wi??cej szczeg??????w dost??pnych we wzorcu rasy). Beagle osi??gaj?? wzrost 33-40 cm i wa???? oko??o 10-11 kg.',
        '100', 'https://www.purina.pl/sites/default/files/2018-02/beagle_400x378_0.jpg');
INSERT INTO product_t
VALUES (2, 1, 'Buldog francuski',
        'Buldog francuski jest ??atwo rozpoznawalny ze wzgl??du na swoje du??e nietoperzowate uszy. Ma p??aski pysk i kr??tk?? l??ni??c?? sier???? o pr??gowanym, nakrapianym lub p??owym umaszczeniu. Doros??y buldog francuski ma 27-34 cm wysoko??ci. Doros??e psy wa???? 12,5 kg, a doros??e suki 11 kg.',
        '200', 'https://www.purina.pl/sites/default/files/2018-02/frenchbull_400x378_0.jpg');
INSERT INTO category_t
VALUES (2, 'Kot');
INSERT INTO product_t
VALUES (3, 2, 'Syberyjski',
        'Z charakteru koty syberyjskie s?? przyjacielskie i uczuciowe. Przywi??zuj?? si?? do opiekuna i lubi?? przebywa?? w ludzkim towarzystwie. S?? to zwierz??ta inteligentne i ??atwo przystosowuj??ce si?? do nowych warunk??w ??ycia. Koty syberyjskie wykazuj?? spor?? aktywno???? i maj?? du??y temperament. S?? te?? skutecznymi ??owcami. Lubi?? si?? wspina?? i odpoczywa?? na wysoko??ciach.',
        '150', 'https://s3-eu-west-1.amazonaws.com/w3.cdn.gpd/pl.whiskas.266/thumb_kot-syberyjski-637201233951607595.jpg');
INSERT INTO product_t
VALUES (4, 2, 'Perski',
        'Z charakteru koty perskie s?? dostojne, ??agodne i zr??wnowa??one. Jak m??wi dr n. wet. Micha?? Ceregrzyn: ???Rzadko pojawiaj?? si?? u nich zachowania agresywne, a je??li wyst??puj??, to wynikaj?? raczej z osobniczych uwarunkowa??.??? Dzi??ki spokojnemu usposobieniu s?? przyja??nie nastawione do ludzi, szybko si?? do nich przywi??zuj?? i uwielbiaj?? by?? w centrum zainteresowania. Persy s?? zazwyczaj ciche i ma??o aktywne, cz??sto wybieraj?? wylegiwanie si?? na kanapie. Nie miaucz?? i nie mrucz?? bez powodu.',
        '300', 'https://s3-eu-west-1.amazonaws.com/w3.cdn.gpd/pl.whiskas.266/thumb_kot-perski-637201233353788448.jpg');

# --- !Downs

DROP TABLE "user_t";
DROP TABLE "address_t";
DROP TABLE "category_t";
DROP TABLE "product_t";
DROP TABLE "cart_t";
DROP TABLE "favourite_product_t";
DROP TABLE "opinion_t";
DROP TABLE "order_t";
DROP TABLE "bought_t";