const path = require('path');
const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin')

module.exports = {
    entry: './src/index.js',
    output: {
        filename: 'bundle.min.js',
        path: path.resolve(__dirname),
        clean: true
    },

    module: {
        rules: [
            {
                test: /\.(js|jsx)$/,
                exclude: /(node_modules|bower_components|build)/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        plugins: ["transform-class-properties"],
                        presets: ['@babel/preset-env', '@babel/react']
                    }
                }
            },
            {
                test: /\.jpe?g|png$/,
                type: 'asset/resource'
            },
            {
                test: /\.(sass|css|scss)$/,
                use: [
                    'style-loader',
                    'css-loader',
                    "sass-loader"
                ]
            },
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
