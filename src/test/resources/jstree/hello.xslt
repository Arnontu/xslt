<xsl:transform version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="/">
		<hello>
			<xsl:value-of select="hello" />
		</hello>
	</xsl:template>
</xsl:transform>
