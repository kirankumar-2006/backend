package com.expensetracker.service;
import com.expensetracker.dto.Dtos.*; import com.expensetracker.entity.*; import com.expensetracker.exception.ApiException; import com.expensetracker.repository.*;
import org.springframework.http.HttpStatus; import org.springframework.stereotype.Service;
import java.time.*; import java.time.temporal.IsoFields; import java.util.*; import java.util.function.Function; import java.util.stream.Stream;
@Service
public class FinanceService {
  private final TransactionRepository txRepo; private final BudgetRepository bRepo; private final UserRepository uRepo;
  public FinanceService(TransactionRepository t,BudgetRepository b,UserRepository u){ txRepo=t; bRepo=b; uRepo=u; }
  User user(String e){ return uRepo.findByEmail(e).orElseThrow(()->new ApiException(HttpStatus.UNAUTHORIZED,"User not found")); }
  List<Transaction> all(String e){ return txRepo.findByUserOrderByDateDesc(user(e)); }
  static double sum(List<Transaction> l,String type){ return l.stream().filter(t->t.type.equals(type)).mapToDouble(t->t.amount).sum(); }

  public Map<String,Object> list(String e,String q,String cat,String type,LocalDate date,int page,int size){
    String s=q==null?"":q.toLowerCase();
    var l=all(e).stream()
      .filter(t->s.isBlank()||t.category.toLowerCase().contains(s)||(t.description!=null&&t.description.toLowerCase().contains(s)))
      .filter(t->cat==null||cat.isBlank()||t.category.equals(cat))
      .filter(t->type==null||type.isBlank()||t.type.equals(type))
      .filter(t->date==null||t.date.equals(date)).toList();
    int from=Math.min(page*size,l.size()),to=Math.min(from+size,l.size());
    return Map.of("content",l.subList(from,to),"totalElements",l.size(),"totalPages",(int)Math.ceil(l.size()/(double)size),"page",page); }
  public Transaction get(String e,Long id){ return txRepo.findByIdAndUser(id,user(e)).orElseThrow(()->ApiException.notFound("Transaction not found")); }
  Transaction apply(Transaction t,TxReq r){
    String type=r.type().toUpperCase(); if(!type.equals("INCOME")&&!type.equals("EXPENSE")) throw ApiException.invalid("Type must be INCOME or EXPENSE");
    t.type=type; t.amount=r.amount(); t.category=r.category(); t.date=r.date(); t.paymentMethod=r.paymentMethod(); t.description=r.description(); return txRepo.save(t); }
  public Transaction create(String e,TxReq r){ Transaction t=new Transaction(); t.user=user(e); return apply(t,r); }
  public Transaction update(String e,Long id,TxReq r){ return apply(get(e,id),r); }
  public void delete(String e,Long id){ txRepo.delete(get(e,id)); }

  double spent(String e,int m,int y){ return all(e).stream().filter(t->t.type.equals("EXPENSE")&&t.date.getMonthValue()==m&&t.date.getYear()==y).mapToDouble(t->t.amount).sum(); }
  Map<String,Object> view(String e,Budget b){ double used=spent(e,b.month,b.year);
    return Map.of("id",b.id,"month",b.month,"year",b.year,"amount",b.amount,"used",used,"remaining",b.amount-used); }
  public List<Map<String,Object>> budgets(String e){ return bRepo.findByUserOrderByYearDescMonthDesc(user(e)).stream().map(b->view(e,b)).toList(); }
  public Map<String,Object> createBudget(String e,BudgetReq r){ User u=user(e);
    if(bRepo.findByUserAndMonthAndYear(u,r.month(),r.year()).isPresent()) throw ApiException.exists("A budget already exists for that month");
    Budget b=new Budget(); b.user=u; b.month=r.month(); b.year=r.year(); b.amount=r.amount(); return view(e,bRepo.save(b)); }
  public Map<String,Object> updateBudget(String e,Long id,BudgetReq r){
    Budget b=bRepo.findByIdAndUser(id,user(e)).orElseThrow(()->ApiException.notFound("Budget not found"));
    b.month=r.month(); b.year=r.year(); b.amount=r.amount(); return view(e,bRepo.save(b)); }
  public void deleteBudget(String e,Long id){ bRepo.delete(bRepo.findByIdAndUser(id,user(e)).orElseThrow(()->ApiException.notFound("Budget not found"))); }

  public Map<String,Object> summary(String e){ var l=all(e); double inc=sum(l,"INCOME"),exp=sum(l,"EXPENSE"); LocalDate now=LocalDate.now();
    double mexp=spent(e,now.getMonthValue(),now.getYear());
    double budget=bRepo.findByUserAndMonthAndYear(user(e),now.getMonthValue(),now.getYear()).map(b->b.amount).orElse(0.0);
    var cats=categories(e);
    return Map.of("totalIncome",inc,"totalExpense",exp,"balance",inc-exp,"monthlyBudget",budget,"monthlyExpense",mexp,
      "remainingBudget",budget-mexp,"topCategory",cats.isEmpty()?"-":cats.get(0).get("label"),"avgDailyExpense",mexp/now.getDayOfMonth(),"transactionCount",l.size()); }
  public List<Map<String,Object>> monthly(String e){ Map<String,double[]> m=new TreeMap<>();
    all(e).forEach(t->m.computeIfAbsent(YearMonth.from(t.date).toString(),k->new double[2])[t.type.equals("INCOME")?0:1]+=t.amount);
    return m.entrySet().stream().map(x->Map.<String,Object>of("label",x.getKey(),"income",x.getValue()[0],"expense",x.getValue()[1])).toList(); }
  public List<Map<String,Object>> categories(String e){ return group(all(e).stream().filter(t->t.type.equals("EXPENSE")),t->t.category).stream()
    .sorted((a,b)->Double.compare((double)b.get("amount"),(double)a.get("amount"))).toList(); }

  List<Map<String,Object>> group(Stream<Transaction> s,Function<Transaction,String> k){ Map<String,Double> m=new TreeMap<>();
    s.forEach(t->m.merge(k.apply(t),t.amount,Double::sum));
    return m.entrySet().stream().map(x->Map.<String,Object>of("label",x.getKey(),"amount",x.getValue())).toList(); }
  public List<Map<String,Object>> daily(String e){ LocalDate from=LocalDate.now().minusDays(30);
    return group(all(e).stream().filter(t->t.type.equals("EXPENSE")&&t.date.isAfter(from)),t->t.date.toString()); }
  public List<Map<String,Object>> weekly(String e){
    return group(all(e).stream().filter(t->t.type.equals("EXPENSE")),t->t.date.getYear()+"-W"+String.format("%02d",t.date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR))); }
  public List<Map<String,Object>> monthlyExpense(String e){ return group(all(e).stream().filter(t->t.type.equals("EXPENSE")),t->YearMonth.from(t.date).toString()); }
}
