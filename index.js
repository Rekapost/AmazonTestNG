const express = require('express');
const app = express();

const PORT = process.env.PORT || 4000;

app.get('/', (req, res) => {
    res.send('Amazon TestNG Framework is Running!');
});

app.listen(PORT, () => {
    console.log(`Amazon TestNG Framework Started on http://localhost:${PORT}`);
});

