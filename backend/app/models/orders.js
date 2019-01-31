module.exports = function (sequelize, DataTypes) {
    return  sequelize.define("orders", {
        id: {
            type:DataTypes.INTEGER,
            autoIncrement:true,
            primaryKey:true
        },
        status: DataTypes.STRING,
        deliveryCost: DataTypes.INTEGER
    });



};