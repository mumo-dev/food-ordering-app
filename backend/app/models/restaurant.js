module.exports = function (sequelize, DataTypes) {
    return sequelize.define("restaurants", {
        id: {
            type:DataTypes.INTEGER,
            autoIncrement:true,
            primaryKey:true
        },
        name: DataTypes.STRING
    });

};