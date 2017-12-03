package sample

import org.seasar.doma.Domain
import scala.beans.BeanProperty

@Domain(valueType = classOf[Long])
case class ID[ENTITY](
    //Domainクラスにはgetter（getXx）が必要
    //@BeanPropertyはgetterとsetterを作ってくれるアノテーション
    @BeanProperty value: Long)
