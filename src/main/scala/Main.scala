import cats.effect.{ExitCode, IO, IOApp}
import com.consolebuilder.renderer.Renderer
import cats.effect.kernel.Sync
import cats.implicits._
import cats.Monad
import cats.data.StateT
import cats.data.StateT._
import cats.effect.kernel.Ref
import eu.timepit.refined.types.string.NonEmptyString
import com.consolebuilder.domain.TextMessage
import com.consolebuilder.Dsl

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    val renderer = Renderer.of[IO]()
    
    def loop2(list: IO[List[String]]): IO[Unit] = {
      Monad[IO].tailRecM[IO[List[String]], Unit](list) { state1 => 
        state1.flatMap { state =>
          state match {
           case e :: other => 
              renderer.render(TextMessage(NonEmptyString.unsafeFrom(e))) *> IO.pure(Left(IO(other)))
           case Nil => IO.pure(Right(IO(List.empty)))
          }
        }
        
      }
    }

    val dsl = Dsl.of[IO]()
    
    val p2 = for
      _ <- dsl.writeMessage("Hello")
      _ <- dsl.writeMessage("World!")
    yield ()

    val out = p2.run(List.empty).map(_._1)
    loop2(out).as(ExitCode.Success)
}
