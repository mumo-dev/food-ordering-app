const  Sequelize = require('sequelize');

const UserModel = require('./models/user');
const TownModel = require('./models/town');
const AreaModel = require('./models/areas');
const RestaurantModel = require('./models/restaurant');
const MenuModel = require('./models/menu');

const config = require('./config/database');

const sequelize = new Sequelize(config.database,config.connection.user, config.connection.password,{
    host:config.connection.host,
    dialect:'mysql',
    pool: {
        max: 10,
        min: 0,
        acquire: 30000,
        idle: 10000
    }
});

const User = UserModel(sequelize,Sequelize);
const Town = TownModel(sequelize,Sequelize);
const Area = AreaModel(sequelize,Sequelize);
const Restaurant = RestaurantModel(sequelize,Sequelize);
const Menu = MenuModel(sequelize,Sequelize);

Area.belongsTo(Town); //town_id added to area
Town.hasMany(Area); //town_id added to area
Restaurant.belongsTo(Town); //town_id added to restaurant
Town.hasMany(Restaurant);
Restaurant.hasMany(Menu);
Menu.belongsTo(Restaurant);


sequelize.sync({force:false})
    .then(()=> console.log('Tables created'))
    .catch(err=> console.log(err));

module.exports ={
    User,
    Town,
    Area,
    Restaurant,
    Menu
};

