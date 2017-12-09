package sample

import org.seasar.doma.Domain
import scala.beans.BeanProperty

//Domainクラスにはvalueのgetter（getValue）が必要
//@scala.beans.BeanPropertyはgetterとsetterを作ってくれる
@Domain(valueType = classOf[Long])
case class ID[ENTITY](
  @BeanProperty value: Long)
