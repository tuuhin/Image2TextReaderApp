package com.eva.image2textreader.domain.models

/**
 * A model containing information about the recognized text
 * @param wholeText The whole text recognized by the model
 * @param textBlocksText Each of the text blocks text
 * @param linesText Each of the lines text
 *
 * @property linesCount count each lines
 * @property wordsCount count each words
 */
data class RecognizedTextModel(
	val wholeText: String,
	val languageCode: String? = null,
	val textBlocksText: List<String>,
	val linesText: List<String>
) {
	val linesCount: Int
		get() = linesText.size

	val wordsCount: Int
		get() = linesText.sumOf { line -> line.count { chr -> chr == ' ' } + 1 }

}
