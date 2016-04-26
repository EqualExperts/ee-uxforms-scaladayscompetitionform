package com.equalexperts.scaladayscompetitionform

import com.uxforms.domain.FormDefinitionFactory
import org.osgi.framework.{BundleActivator, BundleContext}

import scala.collection.JavaConversions._
import scala.collection.mutable.{Map => MMap}

class Activator extends BundleActivator {

  override def start(context: BundleContext): Unit = {
    context.registerService(classOf[FormDefinitionFactory].getName, MyFormDefinitionFactory, MMap[String, Any]())
  }

  override def stop(context: BundleContext): Unit = {}

}