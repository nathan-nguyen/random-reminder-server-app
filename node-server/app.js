const express = require('express');
const app = express();

var winston = require('winston');
var logger = new (winston.Logger)({
        level: 'debug',
        transports: [
                new (winston.transports.Console)({ colorize: true }),
        ]
});

const PORT = 3001;

// Routing
app.use('/', require('./routes/api'));

app.listen(PORT);
console.log('\n------------------------------------------ Server Up ------------------------------------------\n');
logger.info('Running on port ' + PORT);


//*******************************************************************************************************************//

/*

// Using your pre-defined module
var moduleTemplate = require("./module-template/module-template");
var moduleTemplateObject = moduleTemplate();
moduleTemplateObject.printHelloWorld();

*/

//*******************************************************************************************************************//

/*

var db = require("./db/db.js");
var dbInstance = db();

dbInstance.updateData(187, "This is data Id 187");
dbInstance.queryData(187);
dbInstance.queryData(718);

*/

