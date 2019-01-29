const Restaurant = require('../../sequelize').Restaurant;
const Town = require('../../sequelize').Town;
const Menu = require('../../sequelize').Menu;

module.exports = {
    findAllMenus(req, res){
        //paginate req.query.page
        const page = req.query.page;
        const limit =3;
        const offset = page * limit -limit;
        Menu.findAndCount({
            include:[
                {
                    model:Restaurant,
                    as:'restaurant'
                }
            ],
            offset:offset,
            limit: limit
        }).then(menus=>{

            res.status(200).json({
                data:menus.rows,
                total:menus.count,
                hasMore: page * limit < menus.count
            });
        }).catch(err=>{

            res.status(500).json({ error: 'Oops!!, Something went wrong'})
        })

    },


    findAllMenusInTown(req, res){
        const townId = req.params.town_id;
        Menu.findAll({
            include:[
                {
                    model:Restaurant,
                    as:'restaurant',
                    where: { townId: townId}
                }
            ]
        }).then(menus=>{

            res.status(200).json(menus);
        }).catch(err=>{

            res.status(500).json({ error: 'Oops!!, Something went wrong'})
        })

    },
    findMenuById(req, res){
        const id =req.params.id;
        Menu.findById(id, {
            include:[ Restaurant]
        }).then(menu=>{
            if(menu)
            res.status(200).json(menu);
            else
                res.status(404).json({ message: 'Not found'});
        }).catch(err=>{
            res.status(500).json({ error: 'Oops!!, Something went wrong'})
        })
    },


};