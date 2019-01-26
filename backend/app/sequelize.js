const  Sequelize = require('sequelize');

const UserModel = require('./models/user');
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
sequelize.sync({force:false})
    .then(()=> console.log('Tables created'))
    .catch(err=> console.log(err));

module.exports ={
    User
};

