module.exports = function (){

	var db = {};
	var Nedb = require('nedb');
	var data = new Nedb({ filename: './db/data.db', autoload: true });

	db.queryData = function(id) {
		data.find({ _id:  id }, function (err, docs) {
			if (err != null) {
				console.log(err);
				return;
			}

			if (docs.length > 0) {
				console.log("Content for id " + id + ": " + docs[0].content);
			}
			else {
				console.log("Content for id " + id + " does not exist in db");
			}
		});
	}

	db.updateData = function(id, content){
		data.remove({ _id: id}, {}, function (err, numRemoved) {
                	if (err != null) console.log(err);
        	});

		data.insert({ _id: id, content: content}, function (err) {
                	if (err != null) console.log(err);
        	});
	}

	return db;
}
