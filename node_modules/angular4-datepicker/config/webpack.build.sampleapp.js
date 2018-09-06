const path = require('path');
const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
    devtool: 'source-map',

    entry: {
        'vendor': './sampleapp/vendor.ts',
        'app': './sampleapp/main.ts'
    },

    resolve: {
        extensions: ['', '.js', '.ts']
    },

    module: {
        loaders: [
            {
                test: /\.ts$/,
                exclude: [/\.spec\.ts$/],
                loaders: ['awesome-typescript-loader', 'angular2-template-loader']
            },
            {
                test: /\.html$/,
                loader: 'raw'
            },
            {
                test: /\.css$/,
                loader: "to-string-loader!css-loader"
            }
        ]
    },

    plugins: [
        new webpack.NoErrorsPlugin(),
        new webpack.optimize.DedupePlugin(),
        new webpack.optimize.UglifyJsPlugin({ output: {comments: false}, mangle: { screw_ie8 : true, keep_fnames: true} }),
        new webpack.optimize.CommonsChunkPlugin({
            name: ['app', 'vendor']
        }),
        new HtmlWebpackPlugin({
            filename: 'index.html',
            template: 'sampleapp/index.html'
        })
    ],

    output: {
        path: path.resolve('build-sampleapp'),
        publicPath: '/mydatepicker/build-sampleapp/',
        filename: '[name].js',
        chunkFilename: '[id].chunk.js'
    },

    htmlLoader: {
        minimize: false // workaround for ng2
    }
};