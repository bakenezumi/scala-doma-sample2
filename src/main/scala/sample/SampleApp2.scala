package sample

import org.seasar.doma.jdbc.Result

object SampleApp2 extends App {
  private lazy val dao: EmpDao = new EmpDaoImpl(AppConfig)

  AppConfig.getTransactionManager.required({ () =>
    // create table & insert
    dao.create()
    val inserted = Seq(
      Emp(ID[Emp](-1), "scott", 10, -1),
      Emp(ID[Emp](-1), "allen", 20, -1)
    ).map(dao.insert)
    println(inserted)

    // idが2のEmpのageを +1 にupdate
    val updated =
      dao
        .selectById(ID(2))
        .map[Emp](_.growOld) // Optional#mapは型推論が効かないため明示
        .map[Result[Emp]](dao.update)
    println(updated)

    dao.selectAll.forEach(println)
    // =>
    //   Emp(ID(1),scott,10,1)
    //   Emp(ID(2),allen,21,2)
  }: Runnable)

}
