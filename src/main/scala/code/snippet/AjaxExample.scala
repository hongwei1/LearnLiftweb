package code
package snippet

import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.js._
import net.liftweb.util.CssSel
import net.liftweb.util.Helpers._

/**
 * Ajax for processing... it looks a lot like the Stateful example
 */
object AjaxExample {
  def render: CssSel = {
    // state
    var name = ""
    var age = "0"
    val whence = S.referer openOr "/"

    // our process method returns a
    // JsCmd which will be sent back to the browser
    // as part of the response
    def process(): JsCmd = {

      // sleep for 400 millis to allow the user to
      // see the spinning icon
      Thread.sleep(400)
      
      // do the matching
      asInt(age) match {
        // display an error and otherwise do nothing
        case Full(a) if a < 13 => S.error("age", "Too young!"); Noop

        // redirect to the page that the user came from
        // and display notices on that page
        case Full(a) => {
          RedirectTo(whence, () => {
            S.notice("Name: "+name)
            S.notice("Age: "+a)
          })
        }
        
        // more errors
        case _ => S.error("age", "Age doesn't parse as a number"); Noop
      }
    }

    // binding looks normal
    "name=name" #> SHtml.text(name, name = _, "id" -> "the_name") &
    "name=age" #> (SHtml.text(age, age = _) ++ SHtml.hidden(process))
  }
}
