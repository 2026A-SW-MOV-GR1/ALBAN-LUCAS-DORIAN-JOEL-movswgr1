const webpack = require("@nativescript/webpack");

module.exports = (env) => {
	webpack.init(env);

	const config = webpack.resolveConfig();

	// Webpack 5 polyfills for node core modules
	config.resolve.fallback = {
		...(config.resolve.fallback || {}),
		"url": require.resolve("url/"),
		"util": require.resolve("util/"),
		"path": require.resolve("path-browserify"),
		"os": require.resolve("os-browserify/browser"),
		"stream": require.resolve("stream-browserify"),
		"buffer": require.resolve("buffer/"),
		"process": require.resolve("process/browser"),
		"assert": require.resolve("assert/"),
		"fs": false,
		"v8": false
	};

	config.plugins.push(
		new (require('webpack')).ProvidePlugin({
			process: "process/browser",
			Buffer: ["buffer", "Buffer"],
		})
	);

	return config;
};
