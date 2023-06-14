//foods
const food = [
    {
        idfood: "32201",
        foodname: "ayam goreng",
        description:
            'Ayam goreng is an Indonesian and Malaysian dish consisting of chicken deep fried in oil. Ayam goreng literally means "fried chicken" in Malay, Indonesian and also in many Indonesian regional languages (e.g. Javanese).',
        ingredients:
            "1 chicken (about 3 pounds),\n cut into 8 pieces,\n 1 1/2 teaspoons salt,\n 1 1/2 cups flour,\n 1 teaspoon ground pepper,\n vegetable oil",
    },
    {
        idfood: "32202",
        foodname: "ayam pop",
        description:
            "Ayam pop is a fried chicken dish commonly found in Indonesia, consisting of chicken deep fried in oil. The dish contains skinless pale fried chicken that has been boiled or steamed prior to frying.[1] Although ayam pop is identified as ayam goreng (fried chicken), ayam pop is different from common ayam goreng. While fried chicken is golden brown, ayam pop is light-colored. The dish originates from Minangkabau cuisine in West Sumatra.",
        ingredients:
            "1000 gr chicken thighs or breasts,\n1 egg,\n250 ml of ice water,\n 1 tsp Baking soda,\n 10 cloves of garlic,\n 1 teaspoon ground pepper or granules,\n 1 segment of ginger,\n 2 blocks of chicken stock,\n 1 teaspoon salt,\n 1/2 teaspoon coriander,\n 3 red chilies,\n 1 turmeric segment,\n 450 grams of flour,\n 150 grams of cornstarch,\n 2 tsp red chili powder,\n Broth Powder,\n 1 teaspoon of basil powder,\n 1 teaspoon oregano powder,\n 1 teaspoon ground pepper",
    },
    {
        idfood: "32203",
        foodname: "daging rendang",
        description:
            "Rendang is a spicy meat dish originating from Indonesia, typically made with beef slow-cooked in a rich and flavorful sauce of coconut milk and a mixture of spices such as chili, ginger, garlic, and lemongrass.",
        ingredients:
            " 1 kg beef, cut to be about 15 piecies,\n2 liters of coconut milk 3 eggs grated and squeezed coconut old,\n1 stalk lemongrass crushed,\n1 turmeric leaf,\n5 kaffir lime leaves, turmeric leaf fastened with,\n12-20 red papper, ground,\n6 red onions,\n3 cloves garlic,\n1 cm ginger,\n5 cm galangal / laos.",
    },
    {
        idfood: "32204",
        foodname: "dendeng batokok",
        description:
            "Dendeng batokok refers to thinly sliced dried meat in Indonesian cuisine. It is preserved through a mixture of sugar and spices and dried via a frying process. It is similar to jerky. The creation of dendeng is commonly credited to the Minangkabau people, and their earliest dendeng was made from beef, dried so it would be preserved for days and could be taken along with them when they traveled. The Padang cuisine version—probably the most popular dendeng dish in Indonesia—is called dendeng balado or dendeng batokok, and is a speciality from Padang, West Sumatra, made from beef which is thinly cut then dried and fried before adding chillies and other ingredients.",
        ingredients:
            "½ kg of beef,\n1 liter of old coconut water,\n5 red onions,\n5 cloves of garlic,\n1 teaspoon coriander,\n1 teaspoon pepper,\n2 cm galangal,\n2 bay leaves,\n2 pieces of lime leaves,\nSalt to taste,\nThe oil for frying",
    },
    {
        idfood: "32205",
        foodname: "gulai ikan",
        description:
            "Gulai ikan is a class of spicy and rich stew commonly found in Indonesia, Malaysia and Singapore. The main ingredients of this dish fish, as well as vegetables such as cassava leaves, unripe jackfruit and banana stem. Gulai is often described as Indonesian curry, although it is also considered a local dish in Malaysia and Singapore. Gulai is a common name to refer to curry dishes in the country,[1] although Indonesian, Malaysian, and Singaporean cuisine also recognise kari (curry). In Javanese-Surinamese cuisine it is known as guleh.",
        ingredients:
            "3 goldfish, cleaned and gutted,\n2 limes,\n700 ml thick coconut milk,\n5 pieces of curly chili, grind roughly as long as its crushed,\n5 green chilies, coarsely grind as long as they are crushed,\n5 pieces of birds eye chilies, grind coarsely,\n2 stalks of lemongrass, bruised,\n1 kandis tamarind,\n1 sheet of turmeric leaf,\n5 pieces of lime leaves,\n5 cloves of garlic,\n10 shallots,\n5 grains of hazelnut,\n2 segments ginger,\n4 segments of turmeric,\n1 galangal segment,\nsalt to taste",
    },
    {
        idfood: "32206",
        foodname: "gulai tambusu",
        description:
            "gulai tambusu This food is made from the main ingredient of beef intestine. The beef intestine is filled with eggs and tofu. Furthermore, the main ingredient is cooked with various herbs, spices, and coconut milk. Tambusu curry is a soupy dish in which the broth comes from coconut milk.",
        ingredients:
            "1/2 kg of beef intestine,\n3 chicken eggs,\n5 pieces of white tofu, puree,\n2 red onions,\n2 cloves of garlic,\n1 tablespoon ground hazelnut,\n1 teaspoon ground red chili,\nSalt to taste,\nWater to boil,\n1 liter thick coconut milk,\n10 red chilies,\n6 shallots,\n6 cloves of garlic,\n4 candlenuts,\n2 bay leaves,\n2 teaspoons of tamarind water,\n3 lime leaves,\n1 knuckle galangal,\n1 knuckle ginger,\n1 knuckle turmeric,\n1 sheet of turmeric leaf,\n1 teaspoon pepper,\nSalt to taste,\nOil for sautéing",
    },
    {
        idfood: "32207",
        foodname: "gulai tunjang",
        description:
            "gulai tunjang is a food that we can easily find at Padang Restaurants. In West Sumatra, almost evenly in every region, this curry is well known. This food made from cow kikil with thick yellow coconut milk is one of the favorite dishes for fans of Padang cuisine because it has a distinctive taste and is rich in spices.",
        ingredients:
            "1 piece of cow leg,\n 4 glasses of coconut milk from 1 coconut,\n 1 sheet of turmeric leaves,\n 2 sheet of kaffir lime leaves,\n 2 pieces of kandis acid,\n 1 lemongrassBlend,\n 1 ounce of red pepper,\n 10 onions,\n 6 cloves of garlic,\n 5 candlenuts,\n 1 segment of turmeric,\n 1 segment laos,\nsalt to taste",
    },
    {
        idfood: "32208",
        foodname: "telur balado",
        description:
            "telur balado is a typical Minangkabau food which is cooked by sautéing chilies with eggs until absorbed. The pervasive taste of the balado chili sauce will make the eggs more delicious.",
        ingredients:
            "6 hard boiled eggs,\n6 tablespoon cooking oil,\nGrind the following into spice paste,\n10 red chilies,\n50 gram shallot (~ 4 Asian shallots),\n5 cloves garlic,\n2 tomatoes,\n1 tablespoon sugar,\n2 teaspoon salt",
    },
    {
        idfood: "32209",
        foodname: "telur dadar",
        description:
            "telur dadar or omelete is a variation of a fried egg dish prepared by beating eggs and frying them in hot cooking oil or butter in a pan. Omelet and omelete actually only have a slight difference where the texture of the omelete is softer and more tender compared to an omelete. Omelet is a dish that has been chosen as one of the mainstay menus for breakfast because of the ease in the process of making it. Currently there are many types of omelet, some of which are Minang omelet (padang and Korean-style omelet).",
        ingredients:
            "One egg,\nA pinch of salt,\nA pinch of pepper,\n 2 tablespoons of vegetable oil",
    },
];

//nutrition
const nutrisi = [
    {
        idnutrition: "1",
        idfood:"32201",
        kalori: "391",
        lemak: "21,82g",
        karbohidrat: "16,15g",
        protein: "32,90g",
    },
    {
        idnutrition: "2",
        idfood:"32202",
        kalori: "48",
        lemak: "3,01g",
        karbohidrat: "2,61g",
        protein: "2,49g",
    },
    {
        idnutrition: "3",
        idfood:"32203",
        kalori: "468",
        lemak: "26,57g",
        karbohidrat: "10,78g",
        protein: "47,23g",
    },
    {
        idnutrition: "4",
        idfood:"32204",
        kalori: "123",
        lemak: "7,68g",
        karbohidrat: "3,3g",
        protein: "9,96g",
    },
    {
        idnutrition: "5",
        idfood:"32205",
        kalori: "119/100g",
        lemak: "3,5g",
        karbohidrat: "6,1g",
        protein: "15,7g",
    },
    {
        idnutrition: "6",
        idfood:"32206",
        kalori: "130/100g",
        lemak: "7,2g",
        karbohidrat: "1,5g",
        protein: "14g",
    },
    {
        idnutrition: "7",
        idfood:"32207",
        kalori: "200",
        lemak: "9,05g",
        karbohidrat: "8,31g",
        protein: "21,77g",
    },
    {
        idnutrition: "8",
        idfood:"32208",
        kalori: "71",
        lemak: "5,75g",
        karbohidrat: "1,22g",
        protein: "3,57g",
    },
    {
        idnutrition: "9",
        idfood:"32209",
        kalori: "93",
        lemak: "7,33g",
        karbohidrat: "0,42g",
        protein: "6,48g",
    },
];
module.exports = {food,nutrisi}