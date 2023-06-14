'use strict';

const Hapi = require('@hapi/hapi');
const routes = require('./routes');
// const Inert = require('@hapi/inert');
// const Vision = require('@hapi/vision');
// const Handlebars = require('handlebars');

const start = async() =>{
    const server = Hapi.server({
        port:8080,
        host:'0.0.0.0'
    });

    // await server.register(Inert);
    // await server.register(Vision);

    // server.views({
    //     engines:{
    //         html:Handlebars
    //     },
    //     relativeTo: __dirname,
    //     path:'views'
    // });

    server.route(routes);

    await server.start();
    console.log('Server is running on port '+ server.info.uri);
}
start();