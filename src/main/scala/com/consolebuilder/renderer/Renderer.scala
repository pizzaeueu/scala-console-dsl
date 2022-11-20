package com.consolebuilder.renderer

import cats.Applicative
import com.consolebuilder.domain.TextMessage

import com.consolebuilder.domain.*

trait Renderer[F[_]] {
  def render(input: TextMessage): F[Unit]

  def readFromConsole: F[String]
}

object Renderer {
    def of[F[_]: Applicative](): Renderer[F] = new Renderer[F] {

      override def readFromConsole: F[String] =  Applicative[F].pure(scala.Console.in.readLine())        
    
      override def render(message: TextMessage) = Applicative[F].pure(println(Console.BLUE+message.s.value))
    
    }
}
