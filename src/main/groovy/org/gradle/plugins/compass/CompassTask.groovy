package org.gradle.plugins.compass

import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.*
import static org.gradle.plugins.compass.CompassPlugin.CONFIGURATION_NAME

class CompassTask extends JRubyTask {

	String command
	boolean background

	boolean relativeAssets
	boolean boring
	boolean debugInfo
	boolean dryRun
	boolean force
	boolean noLineComments
	boolean quiet
	boolean trace

	String environment
	String outputStyle
	String projectType

	@InputDirectory
	File gemPath

	@OutputDirectory
	File cssDir

	@InputDirectory
	File sassDir

	@InputDirectory
	@Optional
	File fontsDir

	@InputDirectory
	@Optional
	File imagesDir

	@InputDirectory
	@Optional
	File javascriptsDir

	@InputFiles
	@Optional
	FileCollection importPath

	CompassTask() {
		doFirst {
			getCssDir().mkdirs()
		}
	}

	@Override
	protected Iterable<String> getJRubyArguments() {
		def args = []
		args << '-X-C'
		args << '-S' << 'compass' << command
		args << '--sass-dir' << getSassDir()
		args << '--css-dir' << getCssDir()
		if (getImagesDir()) args << '--images-dir' << getImagesDir()
		if (getJavascriptsDir()) args << '--javascripts-dir' << getJavascriptsDir()
		if (getFontsDir()) args << '--fonts-dir' << getFontsDir()

		for (File importDir in getImportPath()?.files) {
			args << '-I' << importDir
		}

		for (gem in getGems()) {
			if (gem != "compass") args << '--require' << gem
		}

		args << '--app' << getProjectType()
		args << '--environment' << getEnvironment()
		args << '--output-style' << getOutputStyle()

		if (getDebugInfo()) {
			args << '--debug-info'
		} else {
			args << '--no-debug-info'
		}

		if (getRelativeAssets()) args << '--relative-assets'
		if (getBoring()) args << '--boring'
		if (getDryRun()) args << '--dry-run'
		if (getForce()) args << '--force'
		if (getNoLineComments()) args << '--no-line-comments'
		if (getQuiet()) args << '--quiet'
		if (getTrace()) args << '--trace'

		return args
	}

	@TaskAction
	@Override
	void jrubyexec() {
		if (background) {
			Thread.start {
				super.jrubyexec()
			}
		} else {
			super.jrubyexec()
		}
	}
}