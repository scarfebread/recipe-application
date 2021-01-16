module.exports = {
    entry: {
        'change-password': './src/change-password.js'
    },
    output: {
        path: __dirname,
        filename: '../resources/static/react/[name].js'
    },
    module: {
        rules: [
            {
                test: /\.js$/,
                exclude: /node_modules/,
                use: {
                    loader: 'babel-loader',
                },
            },
        ],
    },
};