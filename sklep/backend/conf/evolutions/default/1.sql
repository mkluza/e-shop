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
        'Te odważne psy o silnej budowie mają wesołe usposobienie, są zwarte i wysportowane. Ich krótka, gęsta i odporna na warunki pogodowe sierść może mieć różne umaszczenie (więcej szczegółów dostępnych we wzorcu rasy). Beagle osiągają wzrost 33-40 cm i ważą około 10-11 kg.',
        '100', 'https://www.purina.pl/sites/default/files/2018-02/beagle_400x378_0.jpg');
INSERT INTO product_t
VALUES (2, 1, 'Buldog francuski',
        'Buldog francuski jest łatwo rozpoznawalny ze względu na swoje duże nietoperzowate uszy. Ma płaski pysk i krótką lśniącą sierść o pręgowanym, nakrapianym lub płowym umaszczeniu. Dorosły buldog francuski ma 27-34 cm wysokości. Dorosłe psy ważą 12,5 kg, a dorosłe suki 11 kg.',
        '200', 'https://www.purina.pl/sites/default/files/2018-02/frenchbull_400x378_0.jpg');
INSERT INTO category_t
VALUES (2, 'Kot');
INSERT INTO product_t
VALUES (3, 2, 'Syberyjski',
        'Z charakteru koty syberyjskie są przyjacielskie i uczuciowe. Przywiązują się do opiekuna i lubią przebywać w ludzkim towarzystwie. Są to zwierzęta inteligentne i łatwo przystosowujące się do nowych warunków życia. Koty syberyjskie wykazują sporą aktywność i mają duży temperament. Są też skutecznymi łowcami. Lubią się wspinać i odpoczywać na wysokościach.',
        '150', 'https://s3-eu-west-1.amazonaws.com/w3.cdn.gpd/pl.whiskas.266/thumb_kot-syberyjski-637201233951607595.jpg');
INSERT INTO product_t
VALUES (4, 2, 'Perski',
        'Z charakteru koty perskie są dostojne, łagodne i zrównoważone. Jak mówi dr n. wet. Michał Ceregrzyn: „Rzadko pojawiają się u nich zachowania agresywne, a jeśli występują, to wynikają raczej z osobniczych uwarunkowań.” Dzięki spokojnemu usposobieniu są przyjaźnie nastawione do ludzi, szybko się do nich przywiązują i uwielbiają być w centrum zainteresowania. Persy są zazwyczaj ciche i mało aktywne, często wybierają wylegiwanie się na kanapie. Nie miauczą i nie mruczą bez powodu.',
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