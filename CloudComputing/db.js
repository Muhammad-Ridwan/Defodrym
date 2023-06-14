const MySql = require('mysql')

const connection = MySql.createConnection({
    host: '34.128.93.10',
    port: 3306,
    user: 'root',
    password: 'admin123',
    database:'defodrym'
})

connection.connect((err) => {
    if (err) {
        console.log('Koneksi gagal', err)
    } else {
        console.log('Koneksi berhasil')
    }
})

module.exports = connection
