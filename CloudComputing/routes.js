const con = require('./db');
const {food,nutrisi} = require('./food');
const { restart } = require('nodemon');
const route = [
//   {
//   method: 'GET',
//   path: '/',
//   handler: (request, h) => {
//       // Menampilkan konten HTML //
//       return h.view('logo.html').code(200);
//   }
// }, {
//   method: 'GET',
//   path: '/open',
//   handler: (request, h) => {
//       // Menampilkan konten HTML //
//       return h.view('menu.html').code(200);
//   }
// }, {
//   method: 'POST',
//   path: '/galeri',
//   options: {
//       payload: {
//           output: 'stream',
//           parse: true,
//           allow: 'multipart/form-data',
//           multipart: true,
//           maxBytes: 10 * 1024 * 1024,
//       },
//   },
//   handler: async (request, h) => {
//       const {
//           image
//       } = request.payload;
//       if (!image) {
//           const response = h.response({
//               status: 'fail',
//               message: 'Image dibutuhkan'
//           }).code(400);
//           return response;
//       }
//       const PathDirektori = Path.join(__dirname, 'images', image.hapi.filename);
//       const GetImageType = mime.getType(PathDirektori);
//       const writeStream = fs.createWriteStream(PathDirektori);
//       image.pipe(writeStream);
//       await new Promise((resolve, reject) => {
//           writeStream.on('finish', resolve);
//           writeStream.on('error', reject);
//       });
//       const Base64 = fs.readFileSync(PathDirektori, 'base64');
//       const imageUrl = `data:${GetImageType};base64,${Base64}`;
//       return h.view('scan.html', {
//           imageUrl
//       });
//   }
// }, {
//   method: 'POST',
//   path: '/proses',
//   handler: (request, h) => {
//       const {
//           imageUrl
//       } = request.payload;
//       return h.view('proses.html', {
//           imageUrl
//       });
//   }
// },
//     {
//       method: 'POST',
//       path: '/foods',
//       handler: async (request, h) => {
//           try {
//             for(const item of food)
//             {
//               const query1 = 'INSERT INTO foods (idfood, foodname, description, ingredients) VALUES (?, ?, ?, ?)';
//               const foodValues = [item.idfood, item.foodname, item.description, item.ingredients, item.idnutrition];
//               await new Promise((resolve,reject) =>{
//                 con.query(query1,foodValues,(error,result) =>{
//                   if(error){
//                     reject(error);
//                   }else{
//                     resolve();
//                   }
//                 });
//               });
//             }

//             for(const item of nutrisi){
//               const query2 = 'INSERT INTO nutrition (idnutrition, idfood, kalori, lemak, karbohidrat, protein) VALUES (?, ?, ?, ?, ?, ?)';
//               const nutrisivalue = [item.idnutrition, item.idfood, item.kalori, item.lemak, item.karbohidrat, item.protein];
//               await new Promise((resolve,reject) =>{
//                 con.query(query2,nutrisivalue,(error,result) =>{
//                   if(error){
//                     reject(error);
//                   }else{
//                     resolve();
//                   }
//                 });
//               });
//             } 
//               return {
//                   status: 'success',
//                   message: 'Data sudah disimpan',
//               };
//           } catch (error) {
//               console.error(error);
//               return h.response({
//                   status: 'fail',
//                   message: 'Gagal menyimpan data',
//               }).code(400);
//           }
//       },
//   }, 
  {
      method: 'GET',
      path: '/getfooddetails/{foodname}',
      handler: async (request, h) => {
          const {
              foodname
          } = request.params;
          const query = `SELECT foods.*, nutrition.* FROM foods JOIN nutrition ON foods.idfood = nutrition.idfood WHERE foods.foodname = '${foodname}'`;
          try {
              const data = await new Promise((resolve, reject) => {
                  con.query(query, (error, results) => {
                      if (error) {
                          reject(error);
                      } else {
                          resolve(results);
                      }
                  });
              });
              const response = h.response({
                  status: 'success',
                  data,
              });
              response.code(200);
              return response;
          } catch (error) {
              return h.response({
                  error: 'Internal Server Error'
              }).code(500);
          }
      }
  },         
];
module.exports = route;
