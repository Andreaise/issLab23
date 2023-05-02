/* Generated by AN DISI Unibo */ 
package it.unibo.consoleobs

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Consoleobs ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		 var Stopped = false  
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t019",targetState="handlestop",cond=whenDispatch("stopcmd"))
					transition(edgeName="t020",targetState="handleresume",cond=whenDispatch("resumecmd"))
				}	 
				state("handlestop") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if(  Stopped  
						 ){println("consoleobs: already stopped")
						}
						else
						 { Stopped = true  
						 forward("stopappl", "stopappl(console)" ,"appl" ) 
						 }
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
				state("handleresume") { //this:State
					action { //it:State
						if(  Stopped  
						 ){forward("resumeappl", "resumepappl(console)" ,"appl" ) 
						 Stopped = false  
						}
						else
						 {println("consoleobs: resume ignored")
						 }
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
			}
		}
}
