const HtmlWebpackPlugin = require('html-webpack-plugin')
const path = require('path');

module.exports = {
    entry: './src/index.js',
    devtool: 'inline-source-map',
    output: {
        path: path.resolve(__dirname),
        filename: 'bundle.js',
        libraryTarget: 'umd'
    },

    devServer: {
        static: {
            directory:  path.resolve(__dirname) + '\\src',
        },
        compress: true,
        host: 'localhost',
        open: true,
        port: 3000,
        hot: "only",
        onBeforeSetupMiddleware: function (devServer) {
            devServer.app.put('books/update', (req, res) => res.sendStatus(200));
            devServer.app.delete('/books/1', (req, res) => res.sendStatus(200));
            devServer.app.post('/perform_login', (req, res) => res.sendStatus(200))
        }
    },

    module: {
        rules: [
            {
                test: /\.(js|jsx)$/,
                exclude: /(node_modules|bower_components|build)/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['@babel/preset-env', '@babel/react'],
                        plugins: ["transform-class-properties"]
                    }
                }
            },
            {
                test: /\.(sass|css|scss)$/,
                use: [
                    'style-loader',
                    'css-loader',
                    "sass-loader"
                ]
            },
            {
                test: /\.(png|jpg|woff|woff2|eot|ttf|svg)$/,
                type: 'asset/resource'
            },
            {
                test: /\.scss$/,
                use: "sass-loader"
            }
        ]
    },

    plugins: [
        new HtmlWebpackPlugin({
            title: "Your custom title",
            template: './public/index.html'
        })
    ],

    mode: 'development'
};