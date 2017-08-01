package net.pixael.jdt;

import net.pixael.jdt.JDTBase.Type;

public class JSONParser {
	
	private String raw;
	
	public JSONParser(String raw) {
		this.raw = raw;
	}
	
	public JDTBase parse() {
		return this.parse(this.raw);
	}
	
	private JDTBase parse(String raw) {
		JDTBase jdt = null;
		State state = State.STATE_START;
		char[] cs = raw.toCharArray();
		for (int i = 0; i < cs.length; i++) {
			char c = cs[i];
			if (state == State.STATE_START) {
				if (c == '{') {
					state = State.STATE_CREATING_OBJECT;
					jdt = new JDTObject();
					StringBuilder sb = new StringBuilder();
					int level = 0;
					for (; i < cs.length; i++) {
						c = cs[i];
						if (state == State.STATE_CREATING_STRING) {
							if (c == '\\') {
								sb.append(c);
								sb.append(cs[++i]);
							} else if (c == '\"') {
								state = State.STATE_CREATING_OBJECT;
								sb.append(c);
							} else {
								sb.append(c);
							}
						} else if (c == '\"') {
							state = State.STATE_CREATING_STRING;
							sb.append(c);
						} else if (c == '\\') {
							throw new JSONSyntaxException("Invalid character", raw, i);
						} else if (c == '{') {
							if (level > 0) {
								sb.append(c);
							}
							level++;
						} else if (c == '}') {
							level--;
							if (level == 0) {
								this.parse((JDTObject) jdt, sb.toString());
								break;
							} else if (level < 0) {
								throw new JSONSyntaxException("Unbalanced curly brakets", raw, i);
							} else {
								sb.append(c);
							}
						} else {
							sb.append(c);
						}
					}
				} else if (c == '[') {
					state = State.STATE_CREATING_ARRAY;
				} else {
					throw new JSONSyntaxException("Invalid character", raw, i);
				}
			} else {
				throw new JSONSyntaxException("Unable to parse...", raw, i);
			}
		}
		return jdt;
	}
	
	private void parse(JDTObject jdt, String raw) {
		State state = State.STATE_START;
		char[] cs = raw.toCharArray();
		StringBuilder sb = new StringBuilder();
		String tagname = null;
		int brkLevel = 0;
		for (int i = 0; i < cs.length; i++) {
			char c = cs[i];
			//System.out.println(state + " " + i);
			if (state == State.STATE_START) {
				if (c == '\"' || c == '{' || c == '}' || c == '[' || c == ']' || c == ',' || c == ':') {
					throw new JSONSyntaxException("Invalid character", raw, i);
				} else {
					state = State.STATE_CREATING_OBJECT_TAG_NAME;
					sb.append(c);
				}
			} else if (state == State.STATE_CREATING_OBJECT_TAG_NAME) {
				if (c == '\"' || c == '{' || c == '}' || c == '[' || c == ']' || c == ',') {
					throw new JSONSyntaxException("Invalid character", raw, i);
				} else if (c == ':') {
					state = State.STATE_CREATING_OBJECT_TAG_VALUE;
					tagname = sb.toString();
					sb = new StringBuilder();
				} else {
					sb.append(c);
				}
			} else if (state == State.STATE_CREATING_OBJECT_TAG_VALUE) {
				if (c == '\"') {
					state = State.STATE_CREATING_STRING;
				} else if (c >= '0' && c <= '9') {
					state = State.STATE_CREATING_NUMBER;
					i--;
				} else if (c == '[') {
					state = State.STATE_CREATING_ARRAY;
					brkLevel = 1;
				} else if (c == '{') {
					state = State.STATE_CREATING_OBJECT;
					brkLevel = 1;
				} else {
					if (c == 't' || c == 'f') {
						state = State.STATE_CREATING_BOOLEAN;
						sb.append(c);
					} else {
						throw new JSONSyntaxException("Invalid character", raw, i);
					}
				}
			} else if (state == State.STATE_CREATING_STRING) {
				if (c == '\\') {
					if (++i == cs.length) {
						throw new JSONSyntaxException("Invalid character", raw, i - 1);
					}
					c = cs[i];
					if (c == '\"') {
						sb.append(c);
					} else if (c == 'n') {
						sb.append('\n');
					} else if (c == 't') {
						sb.append('\t');
					} else if (c == '\\') {
						sb.append('\\');
					} else {
						throw new JSONSyntaxException("Invalid escaped character", raw, i);
					}
				} else if (c == '\"') {
					int k = i < cs.length - 1 ? i + 1 : i;
					c = cs[k];
					if (c == ',' || i == cs.length - 1) {
						state = State.STATE_CREATING_OBJECT_TAG_NAME;
						JDTString jdtTag = new JDTString();
						jdtTag.name = tagname;
						jdtTag.value = sb.toString();
						jdt.addTag(jdtTag);
						sb = new StringBuilder();
						i++;
					} else {
						throw new JSONSyntaxException("Invalid character", raw, i);
					}
				} else {
					sb.append(c);
				}
			} else if (state == State.STATE_CREATING_NUMBER) {
				int k = i < cs.length - 1 ? i + 1 : i;
				char n = cs[k];
				if (c >= '0' && c <= '9') {
					sb.append(c);
					if (n == ',' || i == cs.length - 1) {
						state = State.STATE_CREATING_OBJECT_TAG_NAME;
						JDTInt jdtTag = new JDTInt();
						jdtTag.name = tagname;
						try {
							jdtTag.value = Integer.parseInt(sb.toString());
						} catch (NumberFormatException nfe) {}
						jdt.addTag(jdtTag);
						sb = new StringBuilder();
						i++;
					}
				} else if (c == 'b' || c == 'B') {
					c = cs[k];
					if (c == ',' || i == cs.length - 1) {
						state = State.STATE_CREATING_OBJECT_TAG_NAME;
						JDTByte jdtTag = new JDTByte();
						jdtTag.name = tagname;
						try {
							jdtTag.value = Byte.parseByte(sb.toString());
						} catch (NumberFormatException nfe) {}
						jdt.addTag(jdtTag);
						sb = new StringBuilder();
						i++;
					} else {
						throw new JSONSyntaxException("Invalid character", raw, i);
					}
				} else if (c == 's' || c == 'S') {
					c = cs[k];
					if (c == ',' || i == cs.length - 1) {
						state = State.STATE_CREATING_OBJECT_TAG_NAME;
						JDTShort jdtTag = new JDTShort();
						jdtTag.name = tagname;
						try {
							jdtTag.value = Short.parseShort(sb.toString());
						} catch (NumberFormatException nfe) {}
						jdt.addTag(jdtTag);
						sb = new StringBuilder();
						i++;
					} else {
						throw new JSONSyntaxException("Invalid character", raw, i);
					}
				} else if (c == 'l' || c == 'L') {
					c = cs[k];
					if (c == ',' || i == cs.length - 1) {
						state = State.STATE_CREATING_OBJECT_TAG_NAME;
						JDTLong jdtTag = new JDTLong();
						jdtTag.name = tagname;
						try {
							jdtTag.value = Long.parseLong(sb.toString());
						} catch (NumberFormatException nfe) {}
						jdt.addTag(jdtTag);
						sb = new StringBuilder();
						i++;
					} else {
						throw new JSONSyntaxException("Invalid character", raw, i);
					}
				} else if (c == 'd' || c == 'f' || c == 'D' || c == 'F') {
					c = cs[k];
					if (c == ',' || i == cs.length - 1) {
						state = State.STATE_CREATING_OBJECT_TAG_NAME;
						JDTDouble jdtTag = new JDTDouble();
						jdtTag.name = tagname;
						try {
							jdtTag.value = Double.parseDouble(sb.toString());
						} catch (NumberFormatException nfe) {}
						jdt.addTag(jdtTag);
						sb = new StringBuilder();
						i++;
					} else {
						throw new JSONSyntaxException("Invalid character", raw, i);
					}
				} else if (c == '.') {
					sb.append(c);
				} else {
					throw new JSONSyntaxException("Invalid character", raw, i);
				}
			} else if (state == State.STATE_CREATING_BOOLEAN) {
				if ((sb.toString() + c).equals("false".substring(0, sb.length() + 1))) {
					sb.append(c);
					int k = i < cs.length ? i + 1 : i;
					c = cs[k];
					if (sb.toString().equals("false")) {
						if (c == ',' || i == cs.length - 1) {
							state = State.STATE_CREATING_OBJECT_TAG_NAME;
							JDTBoolean jdtTag = new JDTBoolean();
							jdtTag.name = tagname;
							jdtTag.value = false;
							jdt.addTag(jdtTag);
							sb = new StringBuilder();
							i++;
						} else {
							throw new JSONSyntaxException("Invalid character", raw, i);
						}
					}
				} else if ((sb.toString() + c).equals("true".substring(0, sb.length() + 1))) {
					sb.append(c);
					if (sb.toString().equals("true")) {
						if (c == ',' || i == cs.length - 1) {
							state = State.STATE_CREATING_OBJECT_TAG_NAME;
							JDTBoolean jdtTag = new JDTBoolean();
							jdtTag.name = tagname;
							jdtTag.value = true;
							jdt.addTag(jdtTag);
							sb = new StringBuilder();
							i++;
						} else {
							throw new JSONSyntaxException("Invalid character", raw, i);
						}
					}
				} else {
					throw new JSONSyntaxException("Invalid character", raw, i - sb.length());
				}
			} else if (state == State.STATE_CREATING_OBJECT) {
				if (c == '{') {
					brkLevel++;
					sb.append(c);
				} else if (c == '}') {
					brkLevel--;
					if (brkLevel == 0) {
						int k = i < cs.length - 1 ? i + 1 : i;
						c = cs[k];
						if (c == ',' || i == cs.length - 1) {
							state = State.STATE_CREATING_OBJECT_TAG_NAME;
							JDTObject jdtTag = new JDTObject();
							jdtTag.name = tagname;
							this.parse(jdtTag, sb.toString());
							jdt.addTag(jdtTag);
							sb = new StringBuilder();
							i++;
						}
					} else if (brkLevel < 0) {
						throw new JSONSyntaxException("Unbalanced curly brakets", raw, i);
					} else {
						sb.append(c);
					}
				} else {
					sb.append(c);
				}
			} else if (state == State.STATE_CREATING_ARRAY) {
				if (c == '[') {
					brkLevel++;
					sb.append(c);
				} else if (c == ']') {
					brkLevel--;
					if (brkLevel == 0) {
						int k = i < cs.length - 1 ? i + 1 : i;
						c = cs[k];
						if (c == ',' || i == cs.length - 1) {
							state = State.STATE_CREATING_OBJECT_TAG_NAME;
							JDTList jdtTag = this.parseList(sb.toString());
							jdtTag.name = tagname;
							jdt.addTag(jdtTag);
							sb = new StringBuilder();
							i++;
						}
					} else if (brkLevel < 0) {
						throw new JSONSyntaxException("Unbalanced square brakets", raw, i);
					} else {
						sb.append(c);
					}
				} else {
					sb.append(c);
				}
			}
		}
	}
	
	private JDTList parseList(String raw) {
		char[] cs = raw.toCharArray();
		Type estimatedType = null;
		char f = cs[0];
		if (f == '\"') {
			estimatedType = Type.STRING;
		} else if (f == '{') {
			estimatedType = Type.OBJECT;
		} else if (f == 'f' || f == 't') {
			estimatedType = Type.BOOLEAN;
		} else if (f >= '0' && f <= '9') {
			estimatedType = Type.INT;
		} else {
			throw new JSONSyntaxException("Invalid character", raw, 0);
		}
		JDTList jdt = new JDTList(estimatedType);
		StringBuilder sb = new StringBuilder();
		Type numberType = Type.INT;
		int level = 0;
		boolean creatingString = false;
		for (int i = 0; i < cs.length; i++) {
			char c = cs[i];
			if (estimatedType == Type.STRING) {
				if (c == '\\') {
					if (i == cs.length - 1 || i == 0) {
						throw new JSONSyntaxException("Invalid character", raw, i);
					} else {
						c = cs[++i];
						if (c == '\"') {
							sb.append('\"');
						} else if (c == 'n') {
							sb.append('\n');
						} else if (c == 't') {
							sb.append('\t');
						} else if (c == '\\') {
							sb.append('\\');
						} else {
							throw new JSONSyntaxException("Invalid escaped character", raw, i);
						}
					}
				} else if (c == '\"') {
					if (i == 0 || i == cs.length - 1) {
					} else {
						c = cs[++i];
						if (c == ',') {
							jdt.addValue(sb.toString());
							sb = new StringBuilder();
						} else {
							throw new JSONSyntaxException("Broken string", raw, i);
						}
					}
				} else {
					sb.append(c);
				}
			} else if (estimatedType == Type.BOOLEAN) {
				sb.append(c);
				if (sb.toString().equals("false".substring(0, sb.length()))) {
					if (sb.toString().equals("false")) {
						int k = i == cs.length - 1 ? i : i + 1;
						c = cs[k];
						if (k == ',' || i == cs.length - 1) {
							jdt.addValue(false);
							sb = new StringBuilder();	
						} else {
							throw new JSONSyntaxException("Invalid character", raw, i);
						}
					}
				} else if (sb.toString().equals("true".substring(0, sb.length()))) {
					if (sb.toString().equals("true")) {
						int k = i == cs.length - 1 ? i : i + 1;
						c = cs[k];
						if (k == ',' || i == cs.length - 1) {
							jdt.addValue(true);
							sb = new StringBuilder();
						} else {
							throw new JSONSyntaxException("Invalid character", raw, k);
						}
					}
				} else {
					throw new JSONSyntaxException("Unable to parse boolean", raw, i);
				}
			} else if (estimatedType == Type.INT) {
				int k = i == cs.length - 1 ? i : i + 1;
				char n = cs[k];
				if (c >= '0' && c <= '9') {
					sb.append(c);
					if (n == ',' || i == cs.length - 1) {
						if (numberType == Type.DOUBLE || numberType == Type.LONG) {
							throw new JSONSyntaxException("Cannot convert from " + numberType.name().toLowerCase() + " to int", raw, i);
						} else {
							try {
								jdt.addValue(Integer.parseInt(sb.toString()));
								sb = new StringBuilder();
								i++;
							} catch (NumberFormatException nfe) {
								throw new JSONSyntaxException("Unable to parse int", raw, i);
							}
						}
					}
				} else if (c == 'b' || c == 'B') {
					if (numberType != Type.BYTE && jdt.size() > 0) {
						throw new JSONSyntaxException("Cannot convert from " + numberType.name().toLowerCase() + " to byte", raw, i);
					} else if (n == ',' || i == cs.length - 1) {
						try {
							numberType = Type.BYTE;
							jdt.addValue(Byte.parseByte(sb.toString()));
							sb = new StringBuilder();
							i++;
						} catch (NumberFormatException nfe) {
							throw new JSONSyntaxException("Unable to parse number", raw, i);
						}
					} else {
						throw new JSONSyntaxException("Unable to parse number", raw, i);
					}
				} else if (c == 's' || c == 'S') {
					if (numberType != Type.SHORT && jdt.size() > 0) {
						throw new JSONSyntaxException("Cannot convert from " + numberType.name().toLowerCase() + " to short", raw, i);
					} else if (n == ',' || i == cs.length - 1) {
						try {
							numberType = Type.SHORT;
							jdt.addValue(Short.parseShort(sb.toString()));
							sb = new StringBuilder();
							i++;
						} catch (NumberFormatException nfe) {
							throw new JSONSyntaxException("Unable to parse number", raw, i);
						}
					} else {
						throw new JSONSyntaxException("Unable to parse number", raw, i);
					}
				} else if (c == 'l' || c == 'L') {
					if (numberType != Type.LONG && jdt.size() > 0) {
						throw new JSONSyntaxException("Cannot convert from " + numberType.name().toLowerCase() + " to long", raw, i);
					} else if (n == ',' || i == cs.length - 1) {
						try {
							numberType = Type.LONG;
							jdt.addValue(Long.parseLong(sb.toString()));
							sb = new StringBuilder();
							i++;
						} catch (NumberFormatException nfe) {
							throw new JSONSyntaxException("Unable to parse number", raw, i);
						}
					} else {
						throw new JSONSyntaxException("Unable to parse number", raw, i);
					}
				} else if (c == 'd' || c == 'D' || c == 'f' || c == 'F') {
					if (n == ',' || i == cs.length - 1) {
						try {
							numberType = Type.DOUBLE;
							jdt.addValue(Double.parseDouble(sb.toString()));
							sb = new StringBuilder();
							i++;
						} catch (NumberFormatException nfe) {
							throw new JSONSyntaxException("Unable to parse number", raw, i);
						}
					} else {
						throw new JSONSyntaxException("Unable to parse number", raw, i);
					}
				} else if (c == '.') {
					if (n == ',' || i == cs.length - 1 || n == '.') {
						throw new JSONSyntaxException("Invalid character", raw, i);
					} else {
						sb.append(c);
					}
				} else {
					throw new JSONSyntaxException("Invalid character", raw, i);
				}
			} else if (estimatedType == Type.OBJECT) {
				if (creatingString) {
					if (c == '\"') {
						creatingString = false;
						sb.append(c);
					} else if (c == '\\') {
						c = cs[++i];
						if (c == ',') {
							throw new JSONSyntaxException("Invalid character", raw, i - 1);
						} else if (c == '\"') {
							sb.append("\\\"");
						} else if (c == 'n') {
							sb.append('\n');
						} else if (c == 't') {
							sb.append('\t');
						} else if (c == '\\') {
							sb.append("\\\\");
						} else {
							throw new JSONSyntaxException("Invalid escaped character", raw, i);
						}
					} else {
						sb.append(c);
					}
				} else if (c == '{') {
					if (level > 0) {
						sb.append(c);
					}
					level++;
				} else if (c == '}') {
					level--;
					if (level == 0) {
						int k = i == cs.length - 1 ? i : i + 1;
						c = cs[k];
						if (c == ',' || i == cs.length - 1) {
							JDTObject jdtObj = new JDTObject();
							this.parse(jdtObj, sb.toString());
							jdt.addValue(jdtObj);
							sb = new StringBuilder();
							i++;
						} else {
							throw new JSONSyntaxException("Invalid character", raw, i + 1);
						}
					} else if (level < 0) {
						throw new JSONSyntaxException("Unbalanced curly brakets", raw, i);
					} else {
						sb.append(c);
					}
				} else if (c == '\"') {
					creatingString = true;
					sb.append(c);
				} else {
					sb.append(c);
				}
			}
		}
		return jdt;
	}
	
	private static enum State {
		STATE_CREATING_OBJECT,
		STATE_CREATING_OBJECT_TAG_NAME,
		STATE_CREATING_OBJECT_TAG_VALUE,
		STATE_CREATING_ARRAY,
		STATE_CREATING_STRING,
		STATE_CREATING_NUMBER,
		STATE_CREATING_BOOLEAN,
		STATE_START;
	}
}
