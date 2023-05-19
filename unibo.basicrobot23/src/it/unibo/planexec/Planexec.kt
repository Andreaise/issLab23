/* Generated by AN DISI Unibo */ 
package it.unibo.planexec

import it.unibo.kactor.*
import alice.tuprolog.*
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Planexec ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		 var Path = ""
				var CurMoveTodo   = ""		
				var StepSynchRes  = false
				var StepTime      = 345L
				var Owner         = "unknown"
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						subscribeToLocalActor("engager") 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("work") { //this:State
					action { //it:State
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t07",targetState="doplan",cond=whenRequest("doplan"))
				}	 
				state("doplan") { //this:State
					action { //it:State
						CommUtils.outred("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						if( checkMsgContent( Term.createTerm("doplan(PATH,OWNER,STEPTIME)"), Term.createTerm("doplan(PLAN,OWNER,STEPTIME)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								CommUtils.outblue("$name | ${payloadArg(0)}")
								  Path  = payloadArg(0).replace("[","").replace("]","").replace(",","").replace(" ","")
												Owner    = payloadArg(1)
												StepTime = payloadArg(2).toLong()
								CommUtils.outblue("$name | Path=$Path")
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_doplan", 
				 	 					  scope, context!!, "local_tout_planexec_doplan", 100.toLong() )
					}	 	 
					 transition(edgeName="t08",targetState="nextMove",cond=whenTimeout("local_tout_planexec_doplan"))   
					transition(edgeName="t09",targetState="pathinterrupted",cond=whenEvent("alarm"))
				}	 
				state("nextMove") { //this:State
					action { //it:State
						 
								   if( Path.length > 0  ){
								   	CurMoveTodo =  Path.elementAt(0).toString() 
								   	Path        =  Path.removePrefix(CurMoveTodo)
								   }else CurMoveTodo = ""		   
						CommUtils.outblue("planexec CurMoveTodo= $CurMoveTodo remain:$Path")
						forward("nextmove", "nextmove($CurMoveTodo)" ,"planexec" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t010",targetState="pathinterrupted",cond=whenEvent("alarm"))
					transition(edgeName="t011",targetState="doMove",cond=whenDispatch("nextmove"))
				}	 
				state("doMove") { //this:State
					action { //it:State
						CommUtils.outblue("domove $CurMoveTodo")
						if(  CurMoveTodo == ""  
						 ){forward("nomoremove", "nomoremove(end)" ,"planexec" ) 
						}
						else
						 {if(  CurMoveTodo == "w"  
						  ){delay(300) 
						  StepSynchRes = uniborobots.robotSupport.dostep( StepTime )  
						 }
						 else
						  {CommUtils.outblue("doMoveTurn $CurMoveTodo")
						  uniborobots.robotSupport.move( CurMoveTodo  )
						   StepSynchRes = true  
						  }
						 forward("nextmove", "nextmove(goon)" ,"planexec" ) 
						 }
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t012",targetState="pathinterrupted",cond=whenEvent("alarm"))
					transition(edgeName="t013",targetState="pathend",cond=whenDispatch("nomoremove"))
					transition(edgeName="t014",targetState="nextMove",cond=whenDispatchGuarded("nextmove",{ StepSynchRes  
					}))
					transition(edgeName="t015",targetState="pathinterrupted",cond=whenDispatchGuarded("nextmove",{ ! StepSynchRes  
					}))
				}	 
				state("pathend") { //this:State
					action { //it:State
						CommUtils.outred("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						if(  currentMsg.msgContent() == "alarm(disengaged)"  
						 ){}
						else
						 {if(  currentMsg.msgId() == "alarm"  
						  ){CommUtils.outblue("pathend  ")
						  val Pathtodo = CurMoveTodo + Path  
						 answer("doplan", "doplanfailed", "doplanfailed($Pathtodo)"   )  
						 }
						 else
						  {if(  StepSynchRes  
						   ){CommUtils.outblue("pathend ok since StepSynchRes=$StepSynchRes ")
						  answer("doplan", "doplandone", "doplandone(ok)"   )  
						  }
						  else
						   { var Pathtodo = CurMoveTodo + Path 
						   					   if( Pathtodo.length == 0 ) Pathtodo="e"
						   CommUtils.outblue("pathend Pathtodo=$Pathtodo ")
						   answer("doplan", "doplanfailed", "doplanfailed($Pathtodo)"   )  
						   }
						  }
						 }
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("pathinterrupted") { //this:State
					action { //it:State
						CommUtils.outmagenta("pathinterrupted  ")
						 var Pathtodo = "" 
						 		   if( StepSynchRes ) Pathtodo = Path  
						 		   else Pathtodo = CurMoveTodo + Path 	
						answer("doplan", "doplanfailed", "doplanfailed($Pathtodo)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
			}
		}
}
