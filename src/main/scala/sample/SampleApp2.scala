package sample

import java.util.Optional
import org.seasar.doma.jdbc.Result

object SampleApp2 extends App {
  private lazy val dao: EmpDao = new EmpDaoImpl(AppConfig)

  AppConfig.getTransactionManager.required({ () =>
    dao.create() // create table
    val inserted = Seq(
      Emp(ID[Emp](-1), "scott", 10, -1),
      Emp(ID[Emp](-1), "allen", 20, -1)
    ).map {
      dao.insert
    }
    println(inserted)
    // idが2のEmpのageを +1
    val updated: Optional[Result[Emp]] = // Optionalは型推論効かない
      dao
        .selectById(ID(2))
        .map { emp =>
          dao.update(emp.growOld)
        }
    println(updated)
    val list = dao.selectAll
    list.forEach(println)
  // =>
  //   Emp(ID(1),scott,10,1)
  //   Emp(ID(2),allen,21,2)
  }: Runnable)

}
