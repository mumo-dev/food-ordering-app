const Restaurant = require('../sequelize').Restaurant;
const Town = require('../sequelize').Town;
const Menu = require('../sequelize').Menu;

module.exports = {

    index(req, res) {
        Restaurant.findAll({
            include: [
                {
                    model: Town,
                    as: 'town'
                }
            ]
        }).then((restaurants) => {

            Town.findAll().then(towns => {


                res.render('admin/restaurants', {
                    restaurants,
                    towns,
                    success: req.flash('successMessage'),
                    error: req.flash('errorMessage')
                });

            })

        })


    },

    add(req, res) {
        //validate
        Restaurant.create({
            name: req.body.name,
            townId: req.body.townId
        }).then(() => {
            req.flash('successMessage', 'Restaurant Added');
            res.redirect('/restaurants');
        }).catch(err => {
            req.flash('errorMessage', 'Oops!!, something happened');
            res.redirect('/restaurants');
        })
    },
    update(req, res) {
        //validate
        const values = {
            name: req.body.name,
            townId: req.body.townId
        };
        const selector = {
            where: {id: req.body.id}
        };

        Restaurant.update(values, selector)
            .then(() => {
                req.flash('successMessage', 'Restaurant updated');
                res.redirect('/restaurants');
            })
            .catch(err => {
                req.flash('errorMessage', 'Oops!!, something happened');
                res.redirect('/restaurants');
            })
    },

    delete(req, res) {

        Restaurant.findById(req.body.id)
            .then(restuarant => {
                if (restuarant) {
                    return restuarant.destroy()
                }
            })
            .then(() => {
                req.flash('successMessage', 'Restaurant deleted');
                res.redirect('/restaurants');
            })
            .catch((err) => {

                req.flash('errorMessage', 'Oops!!, something happened');
                res.redirect('/restaurants');
            })

    },

    displayMenu(req, res) {
        Restaurant.findById(req.params.id, {
            include: [
                {
                    model: Menu,
                    as: 'menus'
                }
            ]
        })
            .then(restaurant => {

                res.render('admin/menus', {
                    restaurant,
                    success: req.flash('successMessage'),
                    error: req.flash('errorMessage')
                });
            })
            .catch(err => {
                throw err;
            })

    },

    addMenu(req, res) {
        const restId = req.body.restaurantId;
        //validate
        console.log(req.file);
        Menu.create({
            name: req.body.menuName,
            description: req.body.menuDescription,
            price: req.body.menuPrice,
            restaurantId: restId,
            imageUrl: req.file.filename
            //
        }).then(() => {
            req.flash('successMessage', 'Menu added successfully');
            res.redirect('/restaurants/' + restId);
        }).catch(err => {
            req.flash('errorMessage', 'Oops!!, something happened');
            res.redirect('/restaurants/' + restId);
        })

    },
    updateMenu(req, res){
        const values = {
            name: req.body.menuName,
            price: req.body.menuPrice,
            description: req.body.menuDescription
        };
        const selector = {
            where: {id: req.body.menuId}
        };

        Menu.update(values, selector)
            .then(() => {
                req.flash('successMessage', 'Menu updated');
                res.redirect('/restaurants/'+ req.body.restaurantId);
            })
            .catch(err => {
                req.flash('errorMessage', 'Oops!!, something happened');
                res.redirect('/restaurants/'+ req.body.restaurantId);
            })
    },

    deleteMenu(req, res) {
        //delete the imagePhoto
        //req.body.imageUrl contains the image name on server

        Menu.findById(req.body.id)
            .then(menu => {
                if (menu) {
                    return menu.destroy()
                }
            })
            .then(() => {
                req.flash('successMessage', 'Menu deleted');
                res.redirect('/restaurants/'+ req.body.restaurantId);
            })
            .catch((err) => {
                req.flash('errorMessage', 'Oops!!, something happened');
                res.redirect('/restaurants/'+ req.body.restaurantId);
            })

    },

};