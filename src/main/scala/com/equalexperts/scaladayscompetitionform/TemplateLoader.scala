package com.equalexperts.scaladayscompetitionform

import com.uxforms.dsl.{MustacheRenderEngine, RemoteTemplateResolver}

trait TemplateLoader {
  private val themeName = "uxforms"
  implicit val renderEngine = new MustacheRenderEngine(new RemoteTemplateResolver(themeName, "templates"))
}
