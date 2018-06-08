<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="2.0">	<!-- This stylesheet outputs the book list as a CSV file -->
	<xsl:template match="BOOKLIST">
		<books>
			<xsl:apply-templates select="BOOKS" />
		</books>
	</xsl:template>
	<xsl:template match="BOOKS">
		<xsl:variable name="catArray" select="/BOOKLIST/CATEGORIES" />
		<xsl:for-each select="ITEM">
			<book>
				<title>
					<xsl:value-of select="TITLE" />
				</title>
				<author>
					<xsl:value-of select="AUTHOR" />
				</author>
				<category>
					<xsl:value-of select="$catArray/CATEGORY[@CODE=current()/@CAT]/@DESC" />
				</category>
				<run>1</run>				<!-- <xsl:variable name="catCode" select="./@CAT"></xsl:variable> -->				<!-- <xsl:message> <xsl:value-of select="$catCode"/> </xsl:message> -->
			</book>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>