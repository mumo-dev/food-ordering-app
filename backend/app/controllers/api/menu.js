const Restaurant = require('../../sequelize').Restaurant;
const Town = require('../../sequelize').Town;
const Menu = require('../../sequelize').Menu;
const Area = require('../../sequelize').Area;


module.exports = {
    findAllMenus(req, res) {
        //paginate req.query.page
        // console.log(req.userData);
        const page = req.query.page || 1;
        const limit = 50;
        const offset = page * limit - limit;
        Menu.findAndCount({
            include: [
                {
                    model: Restaurant,
                    as: 'restaurant'
                }
            ],
            offset: offset,
            limit: limit
        }).then(menus => {

            const menusItems = menus.rows;
            let data = [];
           
            menusItems.forEach(function(menuItem){
                 let menu = {
                    id: menuItem.id,
                    name: menuItem.name,
                    description:menuItem.description,
                    price: menuItem.price,
                    imageUrl: menuItem.imageUrl,
                    restaurantName: menuItem.restaurant.name
                 };
                 data.push(menu)
            })


            res.status(200).json({
                data: data,
                total: menus.count,
                hasMore: page * limit < menus.count
            });
        }).catch(err => {

            res.status(500).json({error: 'Oops!!, Something went wrong'})
        })

    },


    findAllMenusInTown(req, res) {
        const townId = req.params.town_id || 1;
        const page = req.query.page;
        const limit = 50;
        const offset = page * limit - limit;
        Menu.findAll({
            include: [
                {
                    model: Restaurant,
                    as: 'restaurant',
                    where: {townId: townId}
                }
            ],
            offset: offset,
            limit: limit
        }).then(menus => {

            res.status(200).json({
                data: menus.rows,
                total: menus.count,
                hasMore: page * limit < menus.count
            });

        }).catch(err => {

            res.status(500).json({error: 'Oops!!, Something went wrong'})
        })

    },
    findMenuById(req, res) {
        const id = req.params.id;
        Menu.findById(id, {
            include: [Restaurant]
        }).then(menu => {
            if (menu)
                res.status(200).json(menu);
            else
                res.status(404).json({message: 'Not found'});
        }).catch(err => {
            res.status(500).json({error: 'Oops!!, Something went wrong'})
        })
    },

    findTowns(req, res) {
        Town.findAll({
            include: [Area]
        }).then(towns => {

            res.status(200).json(towns);

        }).catch(err => {
            res.status(500).json({error: 'Oops!!, Something went wrong'})
        })

    },

    findTownById(req, res) {
        const id = req.params.id;
        Town.findById(id, {
            include: [Area]
        }).then(town => {
            if (town)
                res.status(200).json(town);
            else
                res.status(404).json({message: 'Not found'});
        }).catch(err => {
            res.status(500).json({error: 'Oops!!, Something went wrong'})
        })
    }


};