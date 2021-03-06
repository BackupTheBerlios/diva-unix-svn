SYNTAXDEF artext
FOR <http://art> <http://art/instance>
START System

OPTIONS {
	memoize = "true";
	tokenspace = "0";
	usePredefinedTokens = "false";
}

TOKENS{
		DEFINE SL_COMMENT $'//'(~('\n'|'\r'|'\uffff'))* $ ;
		DEFINE ML_COMMENT $'/*'.*'*/'$ ;
		
		DEFINE ANNOTATION $'@'('A'..'Z' | 'a'..'z' | '0'..'9' | '_' | '-' )+$;
		
		DEFINE STRING_LITERAL $'"'('\\'('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')|('\\''u'('0'..'9'|'a'..'f'|'A'..'F')('0'..'9'|'a'..'f'|'A'..'F')('0'..'9'|'a'..'f'|'A'..'F')('0'..'9'|'a'..'f'|'A'..'F'))|'\\'('0'..'7')|~('\\'|'"'))*'"'$;

		DEFINE T_INSTANCE_STATE $'#ON'|'#OFF'$;
		DEFINE T_PORT_KIND $'provided'|'required'$;
		
		DEFINE T_IMPLEM $'bundle'|'memory'$;
		
		//DEFINE T_OPTIONAL $'optional'$;
		
		//DEFINE QUALIFIED_NAME $('A'..'Z'|'a'..'z'|'_')('A'..'Z'|'a'..'z'|'_'|'-'|'0'..'9')*('.'('A'..'Z'|'a'..'z'|'_'|'-'|'0'..'9')+)*$;

		DEFINE WHITESPACE $(' '|'\t'|'\f')$;
		DEFINE LINEBREAKS $('\r\n'|'\r'|'\n')$;
		
		DEFINE MULTIPLICITY $( ('*') | (('0'..'9')+) )$;
		
		//DEFINE TEXT $('A'..'Z' | 'a'..'z' | '0'..'9' | '_' | '-' )+$;
		DEFINE TEXT $('A'..'Z' | 'a'..'z' | '_' )('A'..'Z' | 'a'..'z' | '0'..'9' | '_' | '-' )* ('.'('A'..'Z' | 'a'..'z' | '0'..'9' | '_' | '-' )+)*$;
}

TOKENSTYLES {

	"TEXT" COLOR #444444;

	"SL_COMMENT"  COLOR #666666;
	"ML_COMMENT"  COLOR #666666;
	
	"STRING_LITERAL" COLOR #0055bb;
	
	
	"system" COLOR #444444, BOLD;
	
	"root" COLOR #007F55, BOLD;
	"primitive" COLOR #007F55, BOLD;
	"composite" COLOR #007F55, BOLD;
	"instance" COLOR #007F55, BOLD;
	 
	"T_INSTANCE_STATE" COLOR #007F55, BOLD; 
	
	":" COLOR #000000;
	"::" COLOR #000000;
	":=" COLOR #000000;
	
	"bind" COLOR #007F55, BOLD;
	"delegate" COLOR #007F55, BOLD;
	"to" COLOR #007F55, BOLD;
	"id" COLOR #007F55, BOLD;
	
	"functional" COLOR #CC8000, BOLD;
	"control" COLOR #CC8000, BOLD;
	
	"service" COLOR #CC8000, BOLD;
	"operation" COLOR #CC8000, BOLD;
	"in" COLOR #CC8000, BOLD;
	"out" COLOR #CC8000, BOLD;
	
	 
	"implementation" COLOR #0055bb , BOLD;
	"T_IMPLEM" COLOR #0055bb , BOLD;
	"OSGiComponent" COLOR #0055bb;
	"implementingClass" COLOR #0055bb, ITALIC;
	"OSGiType" COLOR #0055bb;
	"generateInstanceBundle" COLOR #0055bb , ITALIC;
	"OSGiPort" COLOR #0055bb;
	"serviceId" COLOR #0055bb , ITALIC;
	
	"groups" COLOR #444444, BOLD;
	"instancegroup" COLOR #444444, BOLD;
	"typegroup" COLOR #444444, BOLD;
	"instances" COLOR #444444, BOLD;
	"types" COLOR #444444, BOLD;
	
	"type" COLOR #A22000, BOLD;
	"compositetype" COLOR #A22000, BOLD;
	"port" COLOR #A22000, BOLD;
	"T_PORT_KIND" COLOR #A22000, BOLD;
	"attribute" COLOR #A22000, BOLD;
	"default" COLOR #A22000, BOLD;
	"T_OPTIONAL" COLOR #A22000, BOLD;
	
	"datatype" COLOR #0055bb, BOLD;
	
}

RULES {
		
		System::= "system" #1 name[] ";" !0 ( !0 ( nodes | services | types | dataTypes | groups) )* ;
		
		DataType::= "datatype" #1 name[] ";"  ;
		
		Distrib.Node ::= "node" #1 name[] (#1 "uri" #1 "=" #1 uri[STRING_LITERAL])? "{" !1 components*  !0 "}" ;
		
		Instance.PrimitiveInstance::= "primitive" #1 "instance"  #1 name[] #1 ":" #1 type[] #1 state[T_INSTANCE_STATE] #1 (!1 "implementation"  #1 implem)? !0 "{" ( !1 (attribute | binding) )* !0 "}"  ;
		
		Instance.CompositeInstance::= "composite" #1 "instance"  #1 name[] #1 ":" #1 type[] #1 state[T_INSTANCE_STATE] #1 (!1 "implementation"  #1 implem)? !0 "{" ( !1 (attribute | binding | subComponent | delegation) )* !0 "}"  ;
		
		Instance.TransmissionBinding::= "bind" #1 client[] #1 "to" #1 serverInstance[] "::" server[] ( #1 "(" "id" #1 "=" #1 id[STRING_LITERAL] ")"  )? ;
		
		Instance.DelegationBinding::= "delegate" #1 source[] #1 "to" #1 serverInstance[] "::" exported[] ( #1 "(" "id" #1 "=" #1 id[STRING_LITERAL] ")"  )? ; 
		
		Instance.ValuedAttribute::=  attribute[] #1 ":=" #1 value[STRING_LITERAL] ;
		
		Instance.DictionaryValuedAttribute::= attribute[] #1 ":=" #1 ( entries ("," #1 entries)* )? ;
		
		Instance.DefaultEntry::= "[" #1 key[] #1 "->" #1 value[STRING_LITERAL] #1 "]" ;
		
		Instance.OtherEntry::=   "[" #1 key[STRING_LITERAL] #1 "->" #1 value[STRING_LITERAL] #1 "]" ;
		
		Type.PrimitiveType::= "type" #1 name[] #1 (!1 "implementation"  #1 implem)? !0 "{" ( port | attribute )* !0 "}"  ;
		
		Type.CompositeType::= "compositetype" #1 name[] #1 (!1 "implementation"  #1 implem)? !0 "{" ( port | attribute )* !0 "}"  ;
		
		//Type.Operation::= !1 "operation" #1 name[] "(" ("in" #1 input | "out" #1 output)? ( "," #1 ("in" #1 input | "out" #1 output) )* ")"  ;
		
		Type.Operation::= !1 "operation" #1 name[] "(" (input)? ( "," #1 input )* ")" (":" output)? ( "," #1 output )* ;
		
		Type.Parameter::= type[] #1 name[];
		
		Type.FunctionalService::= "functional" #1 "service" #1 name[] #1 "{" ( operation )* !0 "}"  ;
		
		Type.ControlService::= "control" #1 "service" #1 name[] #1 "{" ( operation )* !0 "}"  ;
		
		Type.Port::= !1 role[T_PORT_KIND] #1 "port" #1 name[] #1 ":" #1 service[] #1 "[" lower[MULTIPLICITY] ".." upper[MULTIPLICITY] "]" (#1 "protocol" #1 "=" #1 protocol[STRING_LITERAL])? (#1 "uri" #1 "=" #1 uri[STRING_LITERAL])?;
		
		Implem.FractalComponent::= "FractalComponent" #1 "<" "controllerDesc" #1 ":" #1 controllerDesc[STRING_LITERAL] #1 "contentDesc" #1 ":" #1 contentDesc[STRING_LITERAL]  ">"  ;
		
		Implem.OSGiComponent::= "OSGiComponent" #1 ":" #1 implementingClass[STRING_LITERAL] ;
		
		//Implem.OSGiPort::= "OSGiPort" #1 ":" #1 serviceId[STRING_LITERAL] ; 
		
		Group.TypeGroup::= "typegroup" #1 name[] #1 "{" ( !1 "types" #1 ":" #1 types[] ("," #1 types[])* )?  ( !1 subGroups)* !0 "}"  ;
		
		Group.InstanceGroup::= "instancegroup" #1 name[] #1 "{" ( !1 "instances" #1 ":" #1 instances[] ("," #1 instances[])* )?  (!1 subGroups)* !0 "}"  ;
		
		Implem.OSGiType::= generateInstanceBundle[T_IMPLEM]  ;
		
		Type.BasicAttribute::= !1 "attribute" #1 name[] #1 ":" #1 type[] ( #1 "default"  #1 defaultValue[STRING_LITERAL] )? ;
		
		Type.Dictionary::= !1 "attribute" #1 name[] #1 ":" "[" type[] #1 "->" #1 valueType[] "]"  ( #1 "default" #1 "{" keys* !0 "}" )?  ;
		
		Type.DictionaryDefaultValue::= !1 "[" #1 key[STRING_LITERAL] #1 ("->" #1 value[STRING_LITERAL] )? #1 "]" ;
		
		Type.PortId ::= name[];
		
		Type.PortCollection::= !1 role[T_PORT_KIND] #1 "port" #1 name[] #1 ":" #1 service[] #1 "{" ids ("," #1 ids)* "}" ;
		
}